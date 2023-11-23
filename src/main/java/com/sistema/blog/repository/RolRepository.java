package com.sistema.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.blog.models.Rol;

import jakarta.transaction.Transactional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
    @Transactional
    public Optional<Rol> findByNombre(String nombre);

}
