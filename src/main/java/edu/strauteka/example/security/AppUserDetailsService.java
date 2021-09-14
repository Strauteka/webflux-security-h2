package edu.strauteka.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final AppUserDetailsRepository appUserDetailsRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("AppUserDetailsService.findByUsername:{}", username);
        return appUserDetailsRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameOrPasswordNotFoundException()))
                .map(user -> user);
    }

    public Mono<AppUserDetails> findAndValidate(String username, String password) {
        return  appUserDetailsRepository.findByUsername(username)
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .map(appUserDetails -> appUserDetails)
                .switchIfEmpty(Mono.error(new UnauthorizedException()));
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Username or password not valid!")
    private static class UsernameOrPasswordNotFoundException extends Exception {
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
    private static class UnauthorizedException extends Exception {
    }
}
