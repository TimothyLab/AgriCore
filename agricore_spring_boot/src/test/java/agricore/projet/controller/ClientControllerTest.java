package agricore.projet.controller;

import agricore.projet.AbstractBddConnectionTest;
import agricore.projet.config.JwtHeaderFilter;
import agricore.projet.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import({SecurityConfig.class, JwtHeaderFilter.class})
public class ClientControllerTest extends AbstractBddConnectionTest {

    @Test
    void shouldGetClients() throws Exception {

        String token = getToken("clientTest", "test");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/client")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }
}
