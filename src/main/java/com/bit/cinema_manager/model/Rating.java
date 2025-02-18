package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Rating {
    private int id;
    private int ratingNo;
    private User user;
    private Movie movie;
    private int score;
    private String review;
}
