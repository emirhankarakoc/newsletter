package com.karakoc.enewsletter.account.requests;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    
    private String email;
    private String password;
}
