package com.example.miniproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberDto {
    private static BCryptPasswordEncoder bCryptPasswordEncoder;
    @Getter
    @AllArgsConstructor
    public static class Post {

        private String username;
        private String password;
        private String email;

    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private String username;
        private String password;
        private String email;

        public void setUsername(String username){
            this.username = username;
        }
        public void setPassword(String password) { this.password = password;}

    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private String username;
        private String email;

    }

}
