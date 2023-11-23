package com.sistema.blog.controller;


import com.sistema.blog.dto.ComentarioDto;
import com.sistema.blog.exceptions.BlogAppExceptions;
import com.sistema.blog.services.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comentarios")
public class ComentarioControllers {

    @Autowired
    private ComentarioService comentarioService;


    @GetMapping("/publicaciones/{publicacionId}/comentarios")
    public List<ComentarioDto> listarComentariosPorPublicacionId(@PathVariable(value = "publicacionId") Long publicacionId) {
        return comentarioService.obtenerComentariosPorPublicacionId(publicacionId);
    }

    @GetMapping("/publicaciones/{publicacionId}/comentarios/{comentarioId}")
    public ResponseEntity<Object> obtenerComentarioPorId(
            @PathVariable(value = "publicacionId") Long publicacionId,
            @PathVariable(value = "comentarioId") Long comentarioId) {


        try {
            ComentarioDto comentarioDto = comentarioService.obtenerComentarioPorId(comentarioId, publicacionId);
            return new ResponseEntity<>(comentarioDto, HttpStatus.OK);
        } catch (BlogAppExceptions e) {
            String mensaje = e.getMensaje();
            
            Map<String, String> errorResponse = Collections.singletonMap("mensaje", mensaje);
            
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/publicaciones/{publicacionId}/comentarios")
    public ResponseEntity<ComentarioDto> guardarComentario(@PathVariable(value = "publicacionId") Long publicacionId,
                                                           @Valid @RequestBody ComentarioDto comentarioDto) {
        return new ResponseEntity<>(comentarioService.crearComentario(publicacionId, comentarioDto), HttpStatus.CREATED);
    }


    @PutMapping("/publicaciones/{publicacionId}/comentarios/{comentarioId}")
    public ResponseEntity<ComentarioDto> actualizarComentario(@PathVariable(value = "publicacionId") Long publicacionId,
                                                               @PathVariable(value = "comentarioId") Long comentarioId,
                                                              @Valid @RequestBody ComentarioDto comentarioDto) {
        return new ResponseEntity<>(comentarioService.actualizarComentario(publicacionId, comentarioId, comentarioDto), HttpStatus.OK);
    }

    @DeleteMapping("/publicaciones/{publicacionId}/comentarios/{comentarioId}")
    public ResponseEntity<Object> eliminarComentario(@PathVariable(value = "publicacionId") Long publicacionId,
                                                      @PathVariable(value = "comentarioId") Long comentarioId) {
        comentarioService.eliminarComentario(publicacionId, comentarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
