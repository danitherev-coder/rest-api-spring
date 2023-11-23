package com.sistema.blog.controller;

import com.sistema.blog.dto.PublicacionDto;
import com.sistema.blog.dto.PublicacionRespuestaDto;
import com.sistema.blog.services.PublicacionService;
import com.sistema.blog.utils.AppConstantes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publicaciones")
public class PublicacionesControllers {

    @Autowired
    private PublicacionService publicacionService;

    @PreAuthorize( "hasRole('ADMIN')") // El usuario admin puede crear publicaciones
    @PostMapping(value = "/crear")
    public ResponseEntity<PublicacionDto> guardarPublicacion(@Valid @RequestBody PublicacionDto publicacionDto) {
        return new ResponseEntity<>(publicacionService.crearPublicacion(publicacionDto), HttpStatus.CREATED) ;
    }


    @GetMapping()
    public PublicacionRespuestaDto listaPublicaciones(
            @RequestParam(value = "pageNo", defaultValue = AppConstantes.NUMERO_DE_PAGINA_POR_DEFECTO, required = false) int numeroDePagina,
            @RequestParam(value = "pageSize", defaultValue = AppConstantes.MEDIDA_DE_PAGINA_POR_DEFECTO, required = false) int medidaDePagina,
            @RequestParam(value = "sortBy", defaultValue = AppConstantes.ORDENAR_POR_POR_DEFECTO, required = false) String ordenarPor,
            @RequestParam(value = "sortDir", defaultValue = AppConstantes.ORDENAR_DIRECCION_POR_DEFECTO, required = false) String sortDir) {

            return publicacionService.obtenerPublicaciones(numeroDePagina, medidaDePagina, ordenarPor, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDto> obtenerPublicacionPorId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(publicacionService.obtenerPublicacionPorId(id), null, 200);
    }


    @PreAuthorize("hasRole('ADMIN')") // El usuario admin puede actualizar publicaciones
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDto> actualizarPublicacion(@Valid @RequestBody PublicacionDto publicacionDto,
                                                                 @PathVariable("id") Long id) {
        return new ResponseEntity<>(publicacionService.actualizarPublicacion(publicacionDto, id), null, 200);
    }


    @PreAuthorize("hasRole('ADMIN')") // El usuario admin puede eliminar publicaciones
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable("id") Long id, Authentication authentication) {
        publicacionService.eliminarPublicacion(id);

        return new ResponseEntity<>("Publicacion eliminada con exito", null, 204);
    }

}
