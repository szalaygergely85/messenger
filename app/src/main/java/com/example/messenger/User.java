package com.example.messenger;

public class User {
    private String email;
    private String name;
    private String id;

    public User() {
    }

    public User(String email, String name, String id) {
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
