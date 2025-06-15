package com.example.backend.DTO;

public class OAuthUser {
    private String email;
    private String name;
    private String profile;
    private String provider;
    private String userid;

    public OAuthUser(String email, String name, String profile, String provider) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.provider = provider;
        this.userid = email.split("@")[0];
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public String getProvider() {
        return provider;
    }

    public String getUserid() {
        return userid;
    }
}
