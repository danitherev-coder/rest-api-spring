package com.sistema.blog.services.impl;


import com.sistema.blog.dto.PublicacionDto;
import com.sistema.blog.dto.PublicacionRespuestaDto;
import com.sistema.blog.exceptions.DuplicateTitleException;
import com.sistema.blog.exceptions.ResourceNotFoundException;
import com.sistema.blog.models.Publicaciones;
import com.sistema.blog.repository.PublicacionRepository;
import com.sistema.blog.services.PublicacionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private PublicacionRepository publicacionRepository;


    
    private PublicacionDto mapearDTO(Publicaciones publicacion){
        PublicacionDto publicacionDto = modelMapper.map(publicacion, PublicacionDto.class);

        return publicacionDto;
    }


    private Publicaciones mapearEntity(PublicacionDto publicacionDto){
        Publicaciones publicacion = modelMapper.map(publicacionDto, Publicaciones.class);
        return publicacion;
    }


    @Override
    @Transactional
    public PublicacionDto crearPublicacion(PublicacionDto publicacionDto) {

        if (publicacionRepository.existsByTitulo(publicacionDto.getTitulo())) {
            throw new DuplicateTitleException("Ya existe una publicación con el mismo título.");
        }

       
        Publicaciones publicacion = mapearEntity(publicacionDto);
       
        Publicaciones nuevaPublicacion = publicacionRepository.save(publicacion);

       
        return mapearDTO(nuevaPublicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionRespuestaDto obtenerPublicaciones(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase((Sort.Direction.ASC.name())) ? Sort.by(ordenarPor).ascending() :
                Sort.by(ordenarPor).descending();

        
        Pageable pageable = PageRequest.of(numeroDePagina, medidaDePagina, sort);

        
        Page<Publicaciones> publicaciones = publicacionRepository.findAll(pageable);

        List<Publicaciones> listaDePublicaciones = publicaciones.getContent();
        
        List<PublicacionDto> contenido =
                listaDePublicaciones.stream().map(this::mapearDTO).collect(Collectors.toList());

        PublicacionRespuestaDto publicacionRespuestaDto = new PublicacionRespuestaDto();
        publicacionRespuestaDto.setContenido(contenido);
        publicacionRespuestaDto.setNumeroPagina(publicaciones.getNumber());
        publicacionRespuestaDto.setMedidaPagina(publicaciones.getSize());
        publicacionRespuestaDto.setTotalElementos(publicaciones.getTotalElements());
        publicacionRespuestaDto.setTotalPaginas(publicaciones.getTotalPages());
        publicacionRespuestaDto.setUltima(publicaciones.isLast());

        return publicacionRespuestaDto;

    }

    @Transactional(readOnly = true)
    @Override
    public PublicacionDto obtenerPublicacionPorId(Long id) {
        
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

        return mapearDTO(publicacion);
    }

    @Override
    @Transactional
    public PublicacionDto actualizarPublicacion(PublicacionDto publicacionDto, Long id) {


        
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

       
        publicacion.setTitulo(publicacionDto.getTitulo());
        publicacion.setDescripcion(publicacionDto.getDescripcion());
        publicacion.setContenido(publicacionDto.getContenido());

       
        Publicaciones publicacionActualizada = publicacionRepository.save(publicacion);

        
        return mapearDTO(publicacionActualizada);
    }

    @Override
    @Transactional
    public void eliminarPublicacion(Long id) {
       
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

       
        publicacionRepository.delete(publicacion);
    }


}
