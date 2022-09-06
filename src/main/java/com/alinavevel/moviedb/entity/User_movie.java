package com.alinavevel.moviedb.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User_movie {

    private int userid;
    private int movieid;
    private boolean favourite;
    private int personal_rating;
    private String notes;

}
