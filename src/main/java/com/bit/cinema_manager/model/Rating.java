package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Rating {
    private int id;
    private int movieId;
    private int userId;
    private int score;
    private String review;
}
