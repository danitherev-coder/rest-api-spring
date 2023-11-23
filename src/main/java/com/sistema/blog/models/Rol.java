package com.sistema.blog.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String nombre;

    @ManyToMany(mappedBy = "roles", targetEntity = Usuario.class, fetch = FetchType.EAGER)
    private Set<Usuario> usuarios = new HashSet<>();

    public Rol() {
        super();
    }

    public Rol(String nombre) {
        super();
        this.nombre = nombre;
    }

    public Rol(String nombre, Set<Usuario> usuarios) {
        super();
        this.nombre = nombre;
        this.usuarios = usuarios;
    }

    public Rol(Long id, String nombre, Set<Usuario> usuarios) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.usuarios = usuarios;
    }
    


    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    

}

