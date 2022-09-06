package com.alinavevel.moviedb.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<String> generateResponse(Object response, boolean favorite, int rating, String notes,  HttpStatus status) {
        Map<String, Object> map = new LinkedHashMap<>();
            map.put("movie", response);
            map.put("favorite", favorite);
            map.put("rating", rating);
            map.put("notes", notes);



        return new ResponseEntity<String>(String.valueOf(map),  status);
    }
}