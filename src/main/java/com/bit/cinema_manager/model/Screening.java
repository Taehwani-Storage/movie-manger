package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Screening {
    private int id;
    private int screeningNo;
    private String runningTime;
    private boolean isOwned;
}
