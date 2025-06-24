package com.example.backend.DTO;

public class OAuthUser {
    private String email;
    private String name;
    private String profile;
    private String provider;
    private String userid;
    private String phone;
    private String businessType;
    private String address;
    private String birthDate;

    public OAuthUser(String email, String name, String profile, String provider) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.provider = provider;
        this.userid = email.split("@")[0];
        this.phone = "";
        this.businessType = "";
        this.address = "";
        this.birthDate = "";
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

    public String getPhone() { return phone; }
    public String getBusinessType() { return businessType; }
    public String getAddress() { return address; }
    public String getBirthDate() { return birthDate; }
}
