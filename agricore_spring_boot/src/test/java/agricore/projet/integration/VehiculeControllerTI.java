package agricore.projet.integration;

import agricore.projet.dto.utilisateur.AuthResponse;
import agricore.projet.model.Client;
import agricore.projet.repository.IDAOUtilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest //Démarre l'application comme si tout était réellement lancé
@AutoConfigureMockMvc // config auto de l'objet MockMvc : permet de simuler des requêtes HTTP sans démarrer tomcat sur un port
public class VehiculeControllerTI {

    @Autowired //injection de dépendance : permet d'éviter le Class class = new class...
    private MockMvc mockMvc;
    @Autowired
    private IDAOUtilisateur daoUtilisateur;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach //création d'un vrai utilisateur en bdd
    void setUp() {
        Client client = new Client();

        client.setLogin("test2");
        client.setPassword(passwordEncoder.encode("test"));
        client.setNom("test2");
        client.setPrenom("test2");
        client.setMail("test@test.fr");

        daoUtilisateur.save(client);
    }
    @AfterEach
    void cleandDb() {
        daoUtilisateur.findByLogin("test2").ifPresent((u -> daoUtilisateur.delete(u)));
    }

    @Test
    void shouldAuthenticateAndReturnToken() throws Exception {

        String json = """
                {
                    "username":"test",
                    "password":"test"
                }
                """; // requete envoyé au endpoint /api/auth : correspond au DTO AuthRequest

        String result = mockMvc.perform( //on simule la vrai requete http
                MockMvcRequestBuilders
                        .post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
        .andExpect(MockMvcResultMatchers.status().isOk()) //verif le code http de la requete register
        .andReturn() // on demande le resultat de la requete
        .getResponse() // on récupère la réponse
        .getContentAsString(); // récupe de body sous forme de string

        //Récup du token
        AuthResponse authResponse = objectMapper.readValue(result, AuthResponse.class); // ici on utiliser objectMapper pour transformer la string en objet AuthResponse

        String token = authResponse.getToken(); // on récuper le token contenu dans la réponse

        //requête authentifier // on test réellement la requete avec l'authentifiaction récupérer au dessus en ajoutant le token dans le header
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/vehicule")
                .header("Authorization","Bearer "+token)
        )
        .andExpect(MockMvcResultMatchers.status().isOk()); // on attend 200
    }

    @Test
    void shouldGetAllVehiculesReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders // demande d'effectuer une requete simulé
                .get("/api/vehicule"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldGetAllVehicules() throws Exception {

    }
}
