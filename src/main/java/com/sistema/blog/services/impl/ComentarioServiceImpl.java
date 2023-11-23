package com.sistema.blog.services.impl;

import com.sistema.blog.dto.ComentarioDto;
import com.sistema.blog.exceptions.BlogAppExceptions;
import com.sistema.blog.exceptions.ResourceNotFoundException;
import com.sistema.blog.models.Comentarios;
import com.sistema.blog.models.Publicaciones;
import com.sistema.blog.repository.ComentarioRepository;
import com.sistema.blog.repository.PublicacionRepository;
import com.sistema.blog.services.ComentarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;



    private ComentarioDto mapearDTO(Comentarios comentario){
        ComentarioDto comentarioDto = modelMapper.map(comentario, ComentarioDto.class);

        return comentarioDto;
    }

    private Comentarios mapearEntity(ComentarioDto comentarioDto){
        Comentarios comentario = modelMapper.map(comentarioDto, Comentarios.class);
        return comentario;
    }



    @Override
    public ComentarioDto crearComentario(Long publicacionId, ComentarioDto comentarioDto) {
        Comentarios comentario = mapearEntity(comentarioDto);
        Publicaciones publicacion =
                publicacionRepository.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException(
                        "Publicacion", "id", publicacionId));

        comentario.setPublicacion(publicacion);
        Comentarios nuevoComentario = comentarioRepository.save(comentario);

        return mapearDTO(nuevoComentario);
    }

    @Override
    public List<ComentarioDto> obtenerComentariosPorPublicacionId(Long publicacionId) {
        List<Comentarios> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        return comentarios.stream().map(this::mapearDTO).toList();
    }



    @Override
    public ComentarioDto obtenerComentarioPorId(Long comentarioId, Long publicacionId) {
        Publicaciones publicacion =
                publicacionRepository.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException(
                        "Publicacion", "id", publicacionId));

        Comentarios comentario = comentarioRepository.findById(comentarioId).orElseThrow(()-> new ResourceNotFoundException(
                "Comentario", "id", comentarioId));

        if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");

        }

        return mapearDTO(comentario);

    }

    @Override
    public ComentarioDto actualizarComentario(Long publicacionId, Long comentarioId, ComentarioDto comentarioDto) {
        Publicaciones publicacion =
                publicacionRepository.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException(
                        "Publicacion", "id", publicacionId));

        Comentarios comentario = comentarioRepository.findById(comentarioId).orElseThrow(()-> new ResourceNotFoundException(
                "Comentario", "id", comentarioId));

        if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
        }


        comentario.setNombre(comentarioDto.getNombre());
        comentario.setEmail(comentarioDto.getEmail());
        comentario.setComentario(comentarioDto.getComentario());

        Comentarios comentarioActualizado = comentarioRepository.save(comentario);

        return mapearDTO(comentarioActualizado);

    }

    @Override
    public void eliminarComentario(Long publicacionId, Long comentarioId) {
        Publicaciones publicacion =
                publicacionRepository.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException(
                        "Publicacion", "id", publicacionId));

        Comentarios comentario = comentarioRepository.findById(comentarioId).orElseThrow(()-> new ResourceNotFoundException(
                "Comentario", "id", comentarioId));

        if (!comentario.getPublicacion().getId().equals(publicacion.getId())) {
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicacion");
        }

        comentarioRepository.delete(comentario);

    }



}
