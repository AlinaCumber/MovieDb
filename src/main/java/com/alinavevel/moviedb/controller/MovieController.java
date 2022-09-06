package com.alinavevel.moviedb.controller;

import com.alinavevel.moviedb.entity.User_movie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.sql.*;

@RestController
@RequestMapping(value = "api")
public class MovieController {

    private static final String INSERT_USERS_SQL = "INSERT INTO user_movie" +
            "  (userid , movieid , favorite , personal_rating , notes ) VALUES " +
            " (?, ?, ?, ?, ?);";




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
            System.out.println(userId);
            try {
                insertMovieDB(user_movie,movie_id);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }




        return init(urlString);
    }








    public void insertMovieDB(User_movie user_movie,int id_movie) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        // Step 1: Establishing a Connection

        try (Connection connection = com.alinavevel.moviedb.controller.H2JDBCUtils.getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setInt(1, user_movie.getUserid() );
            preparedStatement.setInt(2,id_movie);
            preparedStatement.setBoolean(3, user_movie.isFavourite());
            preparedStatement.setInt(4, user_movie.getPersonal_rating());
            preparedStatement.setString(5, user_movie.getNotes());



            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {



            // print SQL exception information

        }




    }



    public int retrieveUserId(String userName) throws SQLException {
        String consulta = "select userid from users where username = '" + userName + "'";
        try(Connection connection = com.alinavevel.moviedb.controller.H2JDBCUtils.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet rs = statement.executeQuery(consulta);

            if(rs.next()) {
                return rs.getInt("userid");
            }
            return 0;

        }
    }


}

