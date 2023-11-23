package com.sistema.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;



public class ComentarioDto {

    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotEmpty(message = "El email no puede estar vacio")
    @Email
    private String email;

    @NotEmpty(message = "El comentario no puede estar vacio")
    @Size(min = 10, max = 200, message = "El comentario debe tener entre 10 y 200 caracteres")
    private String comentario;
    private Long publicacionId;


    public ComentarioDto(Long id, String nombre, String email, String comentario, Long publicacionId) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.comentario = comentario;
        this.publicacionId = publicacionId;
    }

    public ComentarioDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }
}
