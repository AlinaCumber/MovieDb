package com.alinavevel.moviedb.utils;

import com.alinavevel.moviedb.entity.User_movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.*;
import java.util.HashMap;

@Service
public class MovieService {

    WebClient webClient = WebClient.create("https://api.themoviedb.org/3/movie/");

    @Value("${themoviedatabase.api_key}")
    private String api_key;

    private static final String INSERT_USER_SQL = "INSERT INTO user_movie"
            + "  (userid , movieid , favorite , personal_rating , notes ) VALUES " + " (?, ?, ?, ?, ?);";

    private static final String UPDATE_USER_SQL = "UPDATE user_movie SET favorite = ?, personal_rating = ?, notes = ? WHERE userid = ? AND movieid = ?;";

    private static final String GET_USER_SQL = "select userid , movieid , favorite , personal_rating , notes from user_movie where userid =? and movieid=?;";

    public void insertRecord(User_movie user_movie, int id_movie) throws SQLException {

        try (Connection connection = H2JDBCUtils.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            if (user_movie.getPersonal_rating() < 0 || user_movie.getPersonal_rating() > 5) {
                throw new SQLException("Bad personal rating");
            }

            preparedStatement.setInt(1, user_movie.getUserid());
            preparedStatement.setInt(2, id_movie);
            preparedStatement.setBoolean(3, user_movie.isFavourite());
            preparedStatement.setInt(4, user_movie.getPersonal_rating());
            preparedStatement.setString(5, user_movie.getNotes());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRecord(User_movie user_movie, int id_movie) throws SQLException {

        try (Connection connection = H2JDBCUtils.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {

            if (user_movie.getPersonal_rating() < 0 || user_movie.getPersonal_rating() > 5) {
                throw new SQLException("Bad personal rating");
            }

            preparedStatement.setInt(1, (user_movie.isFavourite() ? 1 : 0));
            preparedStatement.setInt(2, user_movie.getPersonal_rating());
            preparedStatement.setString(3, user_movie.getNotes());
            preparedStatement.setInt(4, user_movie.getUserid());
            preparedStatement.setInt(5, id_movie);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User_movie getUser_movieByID(int id_movie, int user_id) throws SQLException {

        try (Connection connection = H2JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_SQL);) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, id_movie);

            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return null;
            } else {
                User_movie user_movie = new User_movie();
                boolean favorite = rs.getBoolean("favorite");
                int personal_rating = rs.getInt("personal_rating");
                String notes = rs.getString("notes");

                user_movie.setFavourite(favorite);
                user_movie.setNotes(notes);
                user_movie.setPersonal_rating(personal_rating);
                return user_movie;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAuthenticatedUserId(String userName) throws SQLException {
        String consulta = "select userid from users where username = '" + userName + "'";
        try (Connection connection = H2JDBCUtils.getConnection(); Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(consulta);

            if (rs.next()) {
                return rs.getInt("userid");
            }
            return 0;

        }
    }

    @Cacheable("movie")
    public HashMap<String, Object> getMovie(int movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("{movie_id}").queryParam("api_key", api_key).build(movieId))
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("popular")
    public HashMap<String, Object> getPopularMovies() {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("popular").queryParam("api_key", api_key).build())
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("top_rated")
    public HashMap<String, Object> getTopRatedMovies() {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("top_rated").queryParam("api_key", api_key).build())
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("images")
    public HashMap<String, Object> getMovieImages(int movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("{movie_id}/images").queryParam("api_key", api_key).build(movieId))
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("credits")
    public HashMap<String, Object> getMovieCredits(int movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("{movie_id}/credits").queryParam("api_key", api_key).build(movieId))
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("keywords")
    public HashMap<String, Object> getMovieKeyWords(int movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("{movie_id}/keywords").queryParam("api_key", api_key).build(movieId))
                .retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("recommendations")
    public HashMap<String, Object> getMovieRecommendations(int movieId) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("{movie_id}/recommendations")
                .queryParam("api_key", api_key).build(movieId)).retrieve().bodyToMono(HashMap.class).block();
    }

    @Cacheable("similar")
    public HashMap<String, Object> getSimilarMovies(int movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("{movie_id}/similar").queryParam("api_key", api_key).build(movieId))
                .retrieve().bodyToMono(HashMap.class).block();
    }

}
