package com.project.reservation_system.global.authentication.user;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String firstName;
    private String lastName;
    private String role;

    // Constructors, Getters, Setters
    public LoginResponseDTO(String token, String username, String firstName, String lastName, String role) {
        this.token = token;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
