package com.sistema.blog.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderGeneration {

    public static void main(String[] args) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Codificar la contraseña con BCrypt
		System.out.println(passwordEncoder.encode("password"));
	} 
    
}
