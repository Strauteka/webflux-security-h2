package edu.strauteka.example.security;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AppUserDetailsRepository extends ReactiveCrudRepository<AppUserDetails, Long> {

    Mono<AppUserDetails> findByUsername(String username);
}
