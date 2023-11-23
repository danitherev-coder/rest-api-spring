package com.sistema.blog.exceptions;

import com.sistema.blog.dto.ErrorDetalles;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// ControllerAdvice permite manejar excepciones de manera global
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarResourceNotFoundException(ResourceNotFoundException exception,
                                                                          WebRequest webRequest){
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogAppExceptions.class)
    public ResponseEntity<ErrorDetalles> manejarBlogAppException(BlogAppExceptions exception,
                                                                          WebRequest webRequest){
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetalles> manejarGlobalException(Exception exception,
                                                                  WebRequest webRequest){
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // metodo para validar los campos de la entidad Publicaciones

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errores = new HashMap<>();


        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError)error).getField();
            String mensaje = error.getDefaultMessage();

            errores.put(nombreCampo, mensaje);
        });

        return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
    }

    // Manejar el error de que YA EXISTE UNA PUBLICACION CON EL MISMO TITULO CUANDO SE CREAR LA PUBLICACION

    @ExceptionHandler(DuplicateTitleException.class)
    public ResponseEntity<ErrorDetalles> handleDuplicateTitleException(DuplicateTitleException exception,
                                                                       WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }


    // Manejar el error de que YA EXISTE UNA PUBLICACION CON EL MISMO TITULO AL ACTUALIZAR UNA PUBLICACION
    // CUANDO SE CREA YA ESTA ARRIBA
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetalles> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                               WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), "Ya existe una publicación con el mismo título.",
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }


    // Manejar error de que el usuario no existe o bad request
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetalles> handleUserNotFoundException(UsernameNotFoundException exception,
                                                                               WebRequest webRequest) {
        ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetalles, HttpStatus.UNAUTHORIZED);
    }

}