package edu.strauteka.example.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class JwtSecret {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public Algorithm createPasswordEncoder() {
        return Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
