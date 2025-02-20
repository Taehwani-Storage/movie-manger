package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Movie {
    private int id;
    private int movieNo;
    private String title;
    private String director;
    private String synopsis;
/*
    private int rating;
*/
    private String runningTime;
}
