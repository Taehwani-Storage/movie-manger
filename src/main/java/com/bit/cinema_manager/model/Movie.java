package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Movie {
    private int id;
    private int movieNo;
    private String title;
    private String director;
    private String synopsis;
    private String runningTime;
}
