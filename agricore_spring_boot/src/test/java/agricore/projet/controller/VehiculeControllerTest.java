package agricore.projet.controller;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import agricore.projet.config.JwtHeaderFilter;
import agricore.projet.config.SecurityConfig;
import agricore.projet.dto.vehicule.request.VehiculeRequestDTO;
import agricore.projet.dto.vehicule.response.VehiculeResponseDTO;
import agricore.projet.exception.VehiculeNotFound;
import agricore.projet.model.TypeVehicule;
import agricore.projet.repository.IDAOAnimal;
import agricore.projet.repository.IDAOPlante;
import agricore.projet.repository.IDAORessource;
import agricore.projet.repository.IDAOVehicule;
import agricore.projet.repository.IDAOZone;
import agricore.projet.services.JpaUserDetailsService;
import agricore.projet.services.JwtUtils;
import agricore.projet.services.VehiculeService;

@WebMvcTest(controllers = VehiculeController.class)
@Import({SecurityConfig.class, JwtHeaderFilter.class})
public class VehiculeControllerTest {

    private final LocalDate DATE_CONTROLE_TECH = LocalDate.now().plusDays(10);
    private final int DELAI_AVANT_CONTROLE = (int) LocalDate.now().until(DATE_CONTROLE_TECH, java.time.temporal.ChronoUnit.DAYS);
    private final int CARBURANT_ACTUEL = 50;
    private final String ZONE_NOM = "Test Zone";
    private final int ZONE_ID = 1;
    private final int VEHICULE_ID = 1;
    private final TypeVehicule TYPE_VEHICULE = TypeVehicule.UTILITAIRE;

    private final VehiculeResponseDTO VEHICULE_RESPONSE_DTO = new VehiculeResponseDTO(VEHICULE_ID, TYPE_VEHICULE, DATE_CONTROLE_TECH, DELAI_AVANT_CONTROLE, CARBURANT_ACTUEL, ZONE_ID, ZONE_NOM);
    private final VehiculeRequestDTO VEHICULE_REQUEST_DTO = new VehiculeRequestDTO(TYPE_VEHICULE, DATE_CONTROLE_TECH, ZONE_ID, CARBURANT_ACTUEL);

    private final VehiculeRequestDTO VEHICULE_REQUEST_DTO_INVALID = new VehiculeRequestDTO(null, null, null, null);

    private final String URL = "/api/vehicule";
    private final String URL_ID = URL + "/" + VEHICULE_ID;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private IDAORessource daoRessource;
    @MockitoBean
    private IDAOAnimal daoAnimal;

    @MockitoBean
    private IDAOPlante daoPlante;

    @MockitoBean
    IDAOVehicule vehiculeRepository;

    @MockitoBean
    IDAOZone daoZone;

    @MockitoBean
    private VehiculeService vehiculeService;

    @Autowired
    private ObjectMapper objectMapper;

   //ALL

    @Test
    public void shouldgetAllVehiculeReturnUnauthorized() throws Exception {
        //given 

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL));
        //then 

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shouldgetAllVehiculeReturnOk() throws Exception {

        //given 
        Mockito.when(vehiculeService.findAllDTO()).thenReturn(List.of(VEHICULE_RESPONSE_DTO));

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(vehiculeService).findAllDTO();
    }


    //BYID

    @Test
    public void shouldgetVehiculeByIdReturnUnauthorized() throws Exception {
        //given 

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL_ID));
        //then 

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shouldgetVehiculeByIdReturnNotFound() throws Exception {

        //given 
        Mockito.when(vehiculeService.findByIdDTO(VEHICULE_ID)).thenThrow(VehiculeNotFound.class);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL_ID));

        //then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(vehiculeService).findByIdDTO(VEHICULE_ID);
    }

    @Test
    @WithMockUser
    public void shouldgetVehiculeByIdReturnOk() throws Exception {

        //given 
        Mockito.when(vehiculeService.findByIdDTO(VEHICULE_ID)).thenReturn(VEHICULE_RESPONSE_DTO);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(URL_ID));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(vehiculeService).findByIdDTO(VEHICULE_ID);
    }


    //CREATE

    @Test
    public void shouldaddVehiculeReturnUnauthorized() throws Exception {
        //given 

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(URL));
        //then 

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shouldaddVehiculeReturnOk() throws Exception {

        //given 
        Mockito.when(vehiculeService.create(VEHICULE_REQUEST_DTO)).thenReturn(VEHICULE_RESPONSE_DTO);
        String json = objectMapper.writeValueAsString(VEHICULE_REQUEST_DTO);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
        .contentType("application/json")
            .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(vehiculeService).create(Mockito.any(VehiculeRequestDTO.class));
    }

    @Test
    @WithMockUser
    public void shouldaddVehiculeReturnBadRequest() throws Exception {

        //given 
        String json = objectMapper.writeValueAsString(VEHICULE_REQUEST_DTO_INVALID);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
        .contentType("application/json")
            .content(json));
        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }





    //UPDATE 

    @Test
    public void shouldupdateVehiculeReturnUnauthorized() throws Exception {
        //given 

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(URL_ID));
        //then 

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shouldupdateVehiculeReturnOk() throws Exception {

        //given 
        Mockito.when(vehiculeService.update(Mockito.anyInt(), Mockito.any(VehiculeRequestDTO.class))).thenReturn(VEHICULE_RESPONSE_DTO);
        String json = objectMapper.writeValueAsString(VEHICULE_REQUEST_DTO);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(URL_ID)
            .contentType("application/json")
            .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(vehiculeService).update(Mockito.anyInt(), Mockito.any(VehiculeRequestDTO.class));
    }

    @Test
    @WithMockUser   
    public void shouldupdateVehiculeReturnBadRequest() throws Exception {

        //given 
        String json = objectMapper.writeValueAsString(VEHICULE_REQUEST_DTO_INVALID);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put(URL_ID)
            .contentType("application/json")
            .content(json));
        
        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    //DELETE

    @Test
    public void shoulddeleteVehiculeReturnUnauthorized() throws Exception {
        //given 

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(URL_ID));
        //then 

        result.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shoulddeleteVehiculeReturnOk() throws Exception {

        //given 
        Mockito.doNothing().when(vehiculeService).delete(VEHICULE_ID);

        //when 
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete(URL_ID));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        verify(vehiculeService).delete(VEHICULE_ID);
    }




}
