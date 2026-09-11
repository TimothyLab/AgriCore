package agricore.projet.integration;

import agricore.projet.AbstractBddConnectionTest;
import agricore.projet.dto.animal.request.UpdateAnimalRequest;
import agricore.projet.model.animal.Animal;
import agricore.projet.model.animal.EspeceAnimal;
import agricore.projet.model.zone.Zone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AnimalControllerTest extends AbstractBddConnectionTest {

    Animal animal = new Animal();

    UpdateAnimalRequest animal2 = new UpdateAnimalRequest();

    @BeforeEach
    void setUp() {

        Zone zone = daoZone.findById(3)
                .orElseThrow(() -> new RuntimeException("Zone 3 introuvable"));

        animal.setDateNaissance(LocalDate.EPOCH);
        animal.setDateVaccination(LocalDate.EPOCH);
        animal.setEspece(EspeceAnimal.CHEVAL);
        animal.setMale(true);
        animal.setZone(zone);

        animal2.setDateNaissance(LocalDate.EPOCH);
        animal2.setDateVaccination(LocalDate.of(2222,1,1));
        animal2.setEspece(EspeceAnimal.CHEVAL);
        animal2.setMale(true);
        animal2.setZoneId(zone.getId());
    }

    @AfterEach
    void cleanAnimalDb() {
        //TODO daoAnimal.findById(animal.getId()).ifPresent(animal -> {daoAnimal.delete(animal);});
    }

    @Test
    void shouldGetAllAnimalsReturnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/animal")
                .header("Authorization", "Bearer " + getToken("fermierTest", "test"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldGetAnimalByIdReturnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/animal/{id}",1)
                .header("Authorization", "Bearer " + getToken("clientTest", "test"))
        ).andExpect(status().isOk());
    }
    @Test
    void shouldInsertAnimalReturnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/animal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animal))
                        .header("Authorization", "Bearer " + getToken("clientTest", "test"))
        ).andExpect(status().isOk())
         .andReturn();
    }

    @Test
    void shouldUpdateAnimalReturnAuthorized() throws Exception {
        Animal animalPut = new Animal();
        animalPut.setDateNaissance(LocalDate.EPOCH);
        animalPut.setDateVaccination(LocalDate.EPOCH);
        animalPut.setEspece(EspeceAnimal.CHEVAL);
        animalPut.setMale(true);

        daoAnimal.save(animalPut);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/animal/{id}",animalPut.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(animal2))
                .header("Authorization", "Bearer " + getToken("clientTest", "test"))
        ).andExpect(status().isOk());

        Animal updatedAnimal = daoAnimal.findById(animalPut.getId()).orElseThrow();

        assertEquals(animal2.isMale(), updatedAnimal.isMale());
        assertEquals(animal2.getEspece(), updatedAnimal.getEspece());
        assertEquals(animal2.getDateNaissance(), updatedAnimal.getDateNaissance());

        daoAnimal.findById(animalPut.getId()).ifPresent(animal -> {daoAnimal.delete(animal);});
    }

    @Test
    void shouldDeleteAnimalReturnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/animal")
                .header("Authorization", "Bearer " + getToken("clientTest", "test"))
        ).andExpect(status().isOk());
    }
}
