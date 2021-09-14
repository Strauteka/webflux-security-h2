package edu.strauteka.example.security.configuration;

import edu.strauteka.example.security.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {
    private final AppUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication.getAuthorities().isEmpty()) {
            return Mono.justOrEmpty(authentication)
                    .flatMap(auth -> {
                        final String username = (String) (authentication).getPrincipal();
                        final String password = (String) (authentication).getCredentials();
                        return userDetailsService.findAndValidate(username, password)
                                .map(userDetails ->
                                        (Authentication) new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                                                userDetails.getPassword(),
                                                userDetails.getAuthorities()));
                    });
        }
        return Mono.just(authentication);
    }
}
