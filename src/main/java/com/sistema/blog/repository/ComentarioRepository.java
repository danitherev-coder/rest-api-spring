package com.sistema.blog.repository;

import com.sistema.blog.models.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentarios, Long> {

    public List<Comentarios> findByPublicacionId(Long publicacionId);

}
