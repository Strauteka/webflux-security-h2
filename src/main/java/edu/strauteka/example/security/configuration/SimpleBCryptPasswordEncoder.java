package edu.strauteka.example.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Profile("BCryptEncoder")
@Configuration
public class SimpleBCryptPasswordEncoder {
    @Bean
    PasswordEncoder passwordEncoder() {
        //default 10
        return new BCryptPasswordEncoder(12);
    }
}
