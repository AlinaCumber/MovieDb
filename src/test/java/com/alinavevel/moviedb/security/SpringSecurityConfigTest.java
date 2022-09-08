package com.alinavevel.moviedb.security;

import com.alinavevel.moviedb.controller.MovieController;
import com.alinavevel.moviedb.utils.MovieService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = MovieController.class)

class SpringSecurityConfigTest  {

  @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;
    @MockBean
    private DataSource dataSource;



    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    public void withMockUser() throws Exception {
        this.mockMvc.perform(get("/api/movie/popular")).andExpect(status().isOk());
    }






}