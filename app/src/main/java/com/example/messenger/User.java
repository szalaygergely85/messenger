package com.example.messenger;

import java.io.Serializable;

public class User implements Serializable {
    private String Id;
    private String Name;

    public User(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
