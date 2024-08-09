package org.iqmanager.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithUserDetails("+79999999999")
public class HeaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loadHeaderTest() throws Exception{
        mockMvc.perform(get("/api/header"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void SearchTest() throws Exception{
        mockMvc.perform(get("/api/search")
                        .param("s","PRODUCT NO DELIVERY ")
                        .param("p","1")
                        .param("q","5 "))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
