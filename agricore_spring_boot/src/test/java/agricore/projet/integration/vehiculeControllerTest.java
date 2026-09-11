package agricore.projet.integration;

import agricore.projet.AbstractBddConnectionTest;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


//@SpringBootTest //Démarre l'application comme si tout était réellement lancé
//@AutoConfigureMockMvc // config auto de l'objet MockMvc : permet de simuler des requêtes HTTP sans démarrer tomcat sur un port
class VehiculeControllerTest extends AbstractBddConnectionTest {

    @Test
    void shouldGetAllVehiculesReturnAuthorized() throws Exception {

        //requête authentifier // on test réellement la requete avec l'authentifiaction récupérer au dessus en ajoutant le token dans le header
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/vehicule")
                        .header("Authorization", "Bearer " + getToken("clientTest", "test"))
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
    void shouldGetTypesVehicules() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/vehicule/types")
                        .header("Authorization", "Bearer " + getToken("clientTest", "test")))
                .andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    void shouldPostAcheterAnimal() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/vehicule/{animalId}/acheterAnimal",1)
                        .param("vehiculeId", "3")
                        .header("Authorization", "Bearer " + getToken("clientTest", "test"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    void shouldPostRecolterPlante() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/vehicule/{planteId}/recolterPlante",7)
                        .param("vehiculeId","3")
                        .header("Authorization", "Bearer " + getToken("clientTest", "test")))
                .andExpect(MockMvcResultMatchers.status().isOk()
                );


    }

    @Test
    void shouldfairePlein() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/vehicule/1/fairePlein")
                        .header("Authorization", "Bearer " + getToken("clientTest", "test")))
                .andExpect(MockMvcResultMatchers.status().isOk()
                );


    }



}
