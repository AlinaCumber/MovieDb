package com.alinavevel.moviedb.controller;

import com.alinavevel.moviedb.entity.User_movie;
import com.google.gson.JsonParser;
import com.alinavevel.moviedb.utils.ResponseHandler;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;




import java.sql.*;

import static com.alinavevel.moviedb.utils.CRUD.*;


@RestController
@RequestMapping(value = "api")
public class MovieController {





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
    public ResponseEntity<JsonObject> getMovieById(@PathVariable int movie_id) throws SQLException {
        String urlString = "https://api.themoviedb.org/3/movie/" + movie_id;

        User_movie user_movie = new User_movie();
        ResponseEntity<String> responseEntity = init(urlString);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            currentUserName = authentication.getName();
            System.out.println(currentUserName);
            int userId = retrieveUserId(currentUserName);
            System.out.println(userId);
            user_movie.setUserid(userId);
            try {

                User_movie user = getUser_movieByID(movie_id, userId);
                String jsonResponse = responseEntity.getBody();
                if(user == null) {

                    String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"false\", \"personal_rating\":\"null\", \"notes\": \"null\"}");

                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

                    return ResponseEntity.ok(jsonObject);

                }
                else {
                    String json = jsonResponse.substring(0, jsonResponse.length()-1).concat(",\"favourite\": \"" + user.isFavourite() + "\", \"personal_rating\":\"" + user.getPersonal_rating() + "\", \"notes\": \"" + user.getNotes() + "\"}");

                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    return ResponseEntity.ok(jsonObject);
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }



        return ResponseEntity.ok(new JsonParser().parse("{}").getAsJsonObject());



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

    @PatchMapping("/movie/{movie_id}")
    public ResponseEntity<String> postMovie(@PathVariable int movie_id, @RequestBody User_movie user_movie) throws SQLException {
        String urlString="https://api.themoviedb.org/3/movie/"+movie_id+"?language=es-ES";
        //Recuperacion del nombre de usuario
        String currentUserName = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //hay autenticacion
            currentUserName = authentication.getName();
            System.out.println(currentUserName);
            int userId = retrieveUserId(currentUserName);
            user_movie.setUserid(userId);
            try {

                User_movie user = getUser_movieByID(movie_id, userId);

                if(user == null) {
                    insertMovieDB(user_movie,movie_id);
                    return ResponseHandler.generateResponse(init(urlString).getBody(), user_movie.isFavourite(), user_movie.getPersonal_rating(),user_movie.getNotes(), HttpStatus.OK);

                }
                else {
                    updateRecord(user_movie,movie_id);

                    System.out.println(ResponseHandler.generateResponse(init(urlString),user_movie.isFavourite(), user_movie.getPersonal_rating(),user_movie.getNotes(), HttpStatus.ACCEPTED));
                    return ResponseHandler.generateResponse(init(urlString),user_movie.isFavourite(), user_movie.getPersonal_rating(),user_movie.getNotes(), HttpStatus.OK);

                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }



        return init(urlString);
    }





}

