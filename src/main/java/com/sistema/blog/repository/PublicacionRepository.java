package com.sistema.blog.repository;

import com.sistema.blog.models.Publicaciones;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PublicacionRepository extends JpaRepository<Publicaciones, Long> {
    boolean existsByTitulo(String titulo);
}
