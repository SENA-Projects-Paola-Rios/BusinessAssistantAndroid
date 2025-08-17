package com.sena.businessassistantandroid.users;

public class User {
    public int id;
    public String name;
    public String email;
    public String role;
    private String password;
    public boolean expanded = false; // para el acordeón

    public User(int id, String name, String email, String role, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // getters y setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
