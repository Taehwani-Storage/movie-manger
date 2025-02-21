package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Review {
    private int id;
    private int movieId;
    private int userId;
    private int score;
    private String comment;
    private boolean isOwned;

}
