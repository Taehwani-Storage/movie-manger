package com.bit.cinema_manager.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private int userNo;
    private String username;
    private String password;
    private String nickname;
    private int role; // 기본값 일반 회원
}
