package com.alinavevel.moviedb.controller;

import com.alinavevel.moviedb.utils.MovieService;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;



import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(value = MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;
    @MockBean
    private DataSource dataSource;

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getMovie() throws Exception {
        HashMap<String, Object> resultForMock = new HashMap<>();
        resultForMock.put("page",1);
        resultForMock.put("total_pages",600);
        resultForMock.put("results",0);
        resultForMock.put("total_results",600);
        Integer id = 550;


        given(movieService.getMovie(id)).willReturn(resultForMock);

        ResultActions response = mockMvc.perform(get("/api/movie/{movie_id}",id));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.page",is(1)))
                .andExpect(jsonPath("$.total_pages",is(600)))
                .andExpect(jsonPath("$.results",is(0)))
                .andExpect(jsonPath("$.total_results",is(600)))
        ;
    }

    @Test
    void postMovie() {
    }

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getPopularMovies() throws Exception {
        this.mockMvc.perform(get("/api/movie/popular")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getTopRatedMovies() throws Exception {
        this.mockMvc.perform(get("/api/movie/top_rated")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getMovieCredits() throws Exception {
        this.mockMvc.perform(get("/api/movie/550/credits")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getMovieImages() throws Exception {
        this.mockMvc.perform(get("/api/movie/550/images")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getMovieKeyWords() throws Exception {
        this.mockMvc.perform(get("/api/movie/550/keywords")).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getMovieRecommendations() {
        this.mockMvc.perform(get("/api/movie/550/recommendations")).andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", password = "codejava", roles = "USER")
    void getSimilarMovies() {
        this.mockMvc.perform(get("/api/movie/550/similar")).andExpect(status().isOk());
    }
}