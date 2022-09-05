package com.alinavevel.moviedb.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "api")
public class MovieController {

    @GetMapping("/toprated")
    public ResponseEntity<String> init(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmNGZkOTE3NDg2NTM3M2UwOGY0NmY4MzRkZmMwMmI4YyIsInN1YiI6IjYzMTVhYzIxMDI4NDIwMDA3YjNhNjk0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VjwWzSHTrO5xJ8VgxQ-9CeZCcv5O7wZbP_g7OEzcVzo");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,request,String.class);

        return responseEntity;
    }

    @GetMapping("movie/top")
    public ResponseEntity<String> getTop(){
        String url = "https://api.themoviedb.org/3/movie/top_rated?language=es-ES%22";
        return init(url);
    }

    @GetMapping("movie/list")
        public ResponseEntity<String> getList(){
            String url = "https://api.themoviedb.org/3/genre/movie/list";
            return init(url);
        }



    @GetMapping("movie/pupular")
    public ResponseEntity<String> getPopular(){
        String url = "https://api.themoviedb.org/3/movie/popular";
        return init(url);
    }

    @GetMapping("/movie/{movie_id}")
    public ResponseEntity<String> getMovieById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id;
        return init(url);
    }

    @GetMapping("/movie/{movie_id}credits")
    public ResponseEntity<String> getMovieCreditsById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "/credits";
        return init(url);
    }

    @GetMapping("/movie/{movie_id}images")
    public ResponseEntity<String> getMovieImagesById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "/images";
        return init(url);
    }

    @GetMapping("/movie/{movie_id}keywords")
    public ResponseEntity<String> getMovieKeywordsById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "/keywords";
        return init(url);
    }

    @GetMapping("/movie/{movie_id}recommendations")
    public ResponseEntity<String> getMovieRecommendationsById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "/recommendations";
        return init(url);
    }

    @GetMapping("/movie/{movie_id}similar")
    public ResponseEntity<String> getMovieSimilarById(@PathVariable int movie_id){
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "/similar";
        return init(url);
    }


}

