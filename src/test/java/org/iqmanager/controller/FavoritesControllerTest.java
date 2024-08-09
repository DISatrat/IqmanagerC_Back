package org.iqmanager.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithUserDetails("+79999999999")
public class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loadFavorites() throws Exception {
        mockMvc.perform(post("/api/favorites")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void addFavTest() throws Exception {
//        mockMvc.perform(post("/api/addFArr")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("[3]"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    public void deleteFromFavoritesTest() throws Exception {
        mockMvc.perform(delete("/api/deleteFav/3")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAllFavTest() throws Exception {
        mockMvc.perform(delete("/api/delFavorites")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
