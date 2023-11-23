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


    // este metodo convierte una ENTITY(modelo) a un DTO
    private PublicacionDto mapearDTO(Publicaciones publicacion){
        PublicacionDto publicacionDto = modelMapper.map(publicacion, PublicacionDto.class);

        return publicacionDto;
    }

    // Mapear entidad a DTO, en este caso es lo contrario al metodo anterior de mapearDTO
    // Convertir DTO a una ENTITY(modelo)
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

       // Primero convertimos el DTO a una ENTITY
        Publicaciones publicacion = mapearEntity(publicacionDto);
        // Guardamos la publicacion en la base de datos
        Publicaciones nuevaPublicacion = publicacionRepository.save(publicacion);

        // Convertimos la ENTITY a un DTO
        return mapearDTO(nuevaPublicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionRespuestaDto obtenerPublicaciones(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir) {
        // Esto me sirve para ordenar los resultados de la consulta, ya sea de forma ascendente o descendente
        // si te das cuenta el ? y el : es parecido al TERNARIO de JAVASCRIPT
        Sort sort = sortDir.equalsIgnoreCase((Sort.Direction.ASC.name())) ? Sort.by(ordenarPor).ascending() :
                Sort.by(ordenarPor).descending();

        //======================= Pageable =======================//
        /*
            Pageable es una interfaz que nos permite paginar los resultados de una consultaEs una interfaz que representa la información
            de paginación, como el número de página, el tamaño de la página, las propiedades de ordenación, etc. En otras palabras,
            Pageable proporciona detalles sobre cómo recuperar un conjunto específico de datos paginados de una fuente de datos.
         */
        Pageable pageable = PageRequest.of(numeroDePagina, medidaDePagina, sort);

        //==================== Page =======================//
        /*
            PAGE es el contenido de la pagina, es decir nos muestra los resultados de la consulta de la primera pagina, segunda pagina, etc.
         */
        Page<Publicaciones> publicaciones = publicacionRepository.findAll(pageable);

        List<Publicaciones> listaDePublicaciones = publicaciones.getContent();
        // el this::mapearDTO es una funcion lambda que se encarga de mapear cada publicacion a un DTO
        // o puede ser escrito como publicaciones.stream().map(publicacion -> mapearDTO(publicacion)).collect(Collectors.toList());
        // tambien podemos usar el for each, pero es mas lenta la ejecucion
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
        // ResourceNotFoundException es una clase que creamos para manejar las excepciones
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

        return mapearDTO(publicacion);
    }

    @Override
    @Transactional
    public PublicacionDto actualizarPublicacion(PublicacionDto publicacionDto, Long id) {


        // Buscar primero la publicacion que se quiere actualizar
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

        // Una vez encontrada la publicacion, lo que nos pasen por los argumentos del metodo actualizarPublicacion
        // que es publicacionDto, lo seteamos a la publicacion que encontramos
        publicacion.setTitulo(publicacionDto.getTitulo());
        publicacion.setDescripcion(publicacionDto.getDescripcion());
        publicacion.setContenido(publicacionDto.getContenido());

        // Guardamos la publicacion actualizada en la base de datos
        Publicaciones publicacionActualizada = publicacionRepository.save(publicacion);

        // Convertimos la ENTITY a un DTO
        return mapearDTO(publicacionActualizada);
    }

    @Override
    @Transactional
    public void eliminarPublicacion(Long id) {
        // Buscar primero la publicacion que se quiere eliminar
        Publicaciones publicacion = publicacionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", id));

        // Eliminamos la publicacion de la base de datos
        publicacionRepository.delete(publicacion);
    }


}
