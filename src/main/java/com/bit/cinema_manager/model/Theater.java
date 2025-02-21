package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class Theater {
    private int id;
    private int theaterNo;
    private String name;
    private String address;
    private String phone;
    private boolean isOwned;

}
