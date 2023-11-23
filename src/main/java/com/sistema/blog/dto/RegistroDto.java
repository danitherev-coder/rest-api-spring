package com.sistema.blog.dto;

public class RegistroDto {

    private String nombre;
    private String username;
    private String email;
    private String password;

    public RegistroDto() {
        super();
    }

    public RegistroDto(String nombre, String username, String email, String password) {
        super();
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    
}
