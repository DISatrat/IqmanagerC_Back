package org.iqmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithUserDetails("+79999999999")
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BasketController basketController;

    @Test
    public void contextLoad() throws Exception {
        assertNotNull(basketController);
    }

    @Test
    public void addFeedBackTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/giveFeedback")
                        .contentType(MediaType.APPLICATION_JSON_VALUE )
                .content("{\"orderId\":\"1\", \"text\": \"norm\", \"stars\": \"5\" }"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
