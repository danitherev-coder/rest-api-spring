package com.sistema.blog.services;

import com.sistema.blog.dto.PublicacionDto;
import com.sistema.blog.dto.PublicacionRespuestaDto;
import org.springframework.stereotype.Service;


@Service
public interface PublicacionService {

    PublicacionDto crearPublicacion(PublicacionDto publicacionDto);

    PublicacionRespuestaDto obtenerPublicaciones(int numeroDePagina, int medidaDePagina, String ordenarPor,
                                                 String sortDir);

    PublicacionDto obtenerPublicacionPorId(Long id);

    PublicacionDto actualizarPublicacion(PublicacionDto publicacionDto, Long id);

    void eliminarPublicacion(Long id);

}
