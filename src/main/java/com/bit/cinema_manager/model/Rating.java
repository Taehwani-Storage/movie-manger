package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Rating {
    private int id;
    private int ratingNo;
    private int movieId;
    private int userId;
    private String review;
}
