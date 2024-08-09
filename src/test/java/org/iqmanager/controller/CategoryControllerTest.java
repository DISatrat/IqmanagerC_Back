package org.iqmanager.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithUserDetails("+79999999999")
public class CategoryControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loadCategories() throws Exception {
        this.mockMvc.perform(get("/api/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void loadAllCategoriesTest() throws Exception {
        this.mockMvc.perform(get("/api/allCategory"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void loadAllPostByCategoryIdTest() throws Exception {
        this.mockMvc.perform(get("/api/postIdsByCategory/13"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void addFavoriteTest() throws Exception {
        this.mockMvc.perform(patch("/api/favorite/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getQuantityPostsTest() throws Exception {
        this.mockMvc.perform(get("/api/getQuantityPosts")
                        .param("id","13")
                        .param("c","Россия")
                        .param("r","Москва и Московская обл."))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
