package com.karakoc.enewsletter.account.requests;


import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
}
