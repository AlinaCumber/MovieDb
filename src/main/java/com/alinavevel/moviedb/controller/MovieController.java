package com.alinavevel.moviedb.controller;

import com.alinavevel.moviedb.entity.User_movie;
import com.alinavevel.moviedb.utils.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.sql.*;
import java.util.HashMap;


@RestController
@RequestMapping(value = "api")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/movie/{movie_id}")
    public HashMap<String, Object> getMovie(@PathVariable("movie_id") int movieId) throws SQLException {

        HashMap<String, Object> movie = movieService.getMovie(movieId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            int userId = movieService.getAuthenticatedUserId(currentUserName);
            User_movie userMovie = movieService.getUser_movieByID(movieId, userId);

            configureMovieResponse(movie, userMovie);

        }

        return movie;
    }

    private void configureMovieResponse(HashMap<String, Object> movie, User_movie userMovie) {
        if (userMovie == null) {

            movie.put("favourite", false);
            movie.put("personal_rating", 0);
            movie.put("notes", "");

        } else {

            movie.put("favourite", userMovie.isFavourite());
            movie.put("personal_rating", userMovie.getPersonal_rating());
            movie.put("notes", userMovie.getNotes());
        }
    }

    @PatchMapping("/movie/{movie_id}")
    public HashMap<String, Object> postMovie(@PathVariable int movie_id, @RequestBody User_movie user_movie)
            throws SQLException {

        String currentUserName = "";
        int userId = 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            currentUserName = authentication.getName();
            System.out.println(currentUserName);
            userId = movieService.getAuthenticatedUserId(currentUserName);
            user_movie.setUserid(userId);
            try {

                User_movie user = movieService.getUser_movieByID(movie_id, userId);

                if (user == null) {
                    movieService.insertRecord(user_movie, movie_id);
                } else {
                    movieService.updateRecord(user_movie, movie_id);
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }

        User_movie userMovie = movieService.getUser_movieByID(movie_id, userId);
        HashMap<String, Object> movie = movieService.getMovie(movie_id);

        configureMovieResponse(movie, userMovie);

        return movie;
    }

    @GetMapping("/movie/popular")
    public HashMap<String, Object> getPopularMovies() {

        return movieService.getPopularMovies();
    }

    @GetMapping("/movie/top_rated")
    public HashMap<String, Object> getTopRatedMovies() {

        return movieService.getTopRatedMovies();
    }

    @GetMapping("/movie/{movie_id}/credits")
    public HashMap<String, Object> getMovieCredits(@PathVariable("movie_id") int movieId) {

        return movieService.getMovieCredits(movieId);
    }

    @GetMapping("/movie/{movie_id}/images")
    public HashMap<String, Object> getMovieImages(@PathVariable("movie_id") int movieId) {

        return movieService.getMovieImages(movieId);
    }

    @GetMapping("/movie/{movie_id}/keywords")
    public HashMap<String, Object> getMovieKeyWords(@PathVariable("movie_id") int movieId) {

        return movieService.getMovieKeyWords(movieId);
    }

    @GetMapping("/movie/{movie_id}/recommendations")
    public HashMap<String, Object> getMovieRecommendations(@PathVariable("movie_id") int movieId) {

        return movieService.getMovieRecommendations(movieId);
    }

    @GetMapping("/movie/{movie_id}/similar")
    public HashMap<String, Object> getSimilarMovies(@PathVariable("movie_id") int movieId) {

        return movieService.getSimilarMovies(movieId);
    }



}

