package com.sistema.blog.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.blog.dto.LoginDto;
import com.sistema.blog.dto.RegistroDto;
import com.sistema.blog.models.Rol;
import com.sistema.blog.models.Usuario;
import com.sistema.blog.repository.RolRepository;
import com.sistema.blog.repository.UsuarioRepository;
import com.sistema.blog.security.JwtAuthResponseDto;
import com.sistema.blog.security.JwtTokenProvider;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private RolRepository rolRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Autowired
  private JwtTokenProvider tokenProvider;


  @PostMapping("/login")
  public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));


      SecurityContextHolder.getContext().setAuthentication(authentication);

      
      String token = tokenProvider.generarToken(authentication);

      return ResponseEntity.ok(new JwtAuthResponseDto(token));

    } catch (Exception e) {
      throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
    }

  }


  @Transactional
  @PostMapping("/signup")
  public ResponseEntity<?> registrarUser(@RequestBody RegistroDto registroDto) {

    if (usuarioRepository.existsByUsername(registroDto.getUsername())) {
      return new ResponseEntity<>("El nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
    }

    if (usuarioRepository.existsByEmail(registroDto.getEmail())) {
      return new ResponseEntity<>("El email ya existe", HttpStatus.BAD_REQUEST);
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(registroDto.getNombre());
    usuario.setUsername(registroDto.getUsername());
    usuario.setEmail(registroDto.getEmail());
    usuario.setPassword(passwordEncoder.encode(registroDto.getPassword()));

    Rol rol = rolRepository.findByNombre("ADMIN").get();
    
    usuario.setRoles(Collections.singleton(rol));

    usuarioRepository.save(usuario);

    return new ResponseEntity<>("Usuario registrado con éxito!!", HttpStatus.OK);
  }

}
