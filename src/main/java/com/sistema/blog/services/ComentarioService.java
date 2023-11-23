package com.sistema.blog.services;

import com.sistema.blog.dto.ComentarioDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComentarioService {

    /*
        Como el comentario necesita de una publicacion, se debe pasar el id de la publicacion
     */
    ComentarioDto crearComentario(Long publicacionId, ComentarioDto comentarioDto);

    List<ComentarioDto> obtenerComentariosPorPublicacionId(Long publicacionId);


    ComentarioDto obtenerComentarioPorId(Long comentarioId, Long publicacionId);

    ComentarioDto actualizarComentario(Long publicacionId, Long comentarioId, ComentarioDto comentarioDto);

    void eliminarComentario(Long publicacionId, Long comentarioId);
}
