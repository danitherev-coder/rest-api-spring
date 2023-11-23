package com.sistema.blog.dto;

import com.sistema.blog.models.Comentarios;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Set;




public class PublicacionDto {

    private Long id;

    @NotEmpty(message = "El titulo no puede estar vacio")
    @Size(min = 2, message = "El titulo debe tener entre 2 y 50 caracteres")
    private String titulo;

    @NotEmpty(message = "La descripcion no puede estar vacia")
    @Size(min = 10, message = "La descripcion debe tener como minimo 10 caracteres")
    private String descripcion;

    @NotEmpty(message = "El contenido no puede estar vacio")
    private String contenido;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Set<Comentarios> comentario;


    public PublicacionDto(Long id, String titulo, String descripcion, String contenido, Date fechaCreacion, Date fechaActualizacion, Set<Comentarios> comentario) {
        super();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.comentario = comentario;
    }



    public PublicacionDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<Comentarios> getComentario() {
        return comentario;
    }

    public void setComentario(Set<Comentarios> comentario) {
        this.comentario = comentario;
    }
}
