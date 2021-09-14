package edu.strauteka.example.security;

import edu.strauteka.example.dto.UserDto;
import edu.strauteka.example.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jwt")
public class JwtAuthorizationController {
    private final AppUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @GetMapping("{username}")
    Mono<UserDetails> findByUsername(@PathVariable("username") String username) {
        return userDetailsService.findByUsername(username);
    }

    @PostMapping("{username}/{password}")
    Mono<ResponseEntity<String>> getToken(@PathVariable("username") String username,
                                          @PathVariable("password") String password,
                                          ServerWebExchange exchange) {
        return userDetailsService.findAndValidate(username, password).map(auth ->
                {
                    final String accessToken = jwtUtils.createAccessToken(auth,
                            exchange.getRequest().getURI().getHost(),
                            JwtUtils.ACCESS_EXPIRATION_TIME);
                    return ResponseEntity.ok(accessToken);
                }
        ).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(notUsed -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PostMapping
    Mono<ResponseEntity<Void>> writeTokenToCookie(@RequestBody UserDto username, ServerWebExchange exchange) {
        return userDetailsService.findAndValidate(username.getUsername(), username.getPassword()).map(auth ->
                {
                    final String accessToken = jwtUtils.createAccessToken(auth,
                            exchange.getRequest().getURI().toString(),
                            JwtUtils.ACCESS_EXPIRATION_TIME);

                    final ResponseCookie cookie = ResponseCookie.fromClientResponse(JwtUtils.JWT_COOKIE_NAME, accessToken)
                            .maxAge(3600)
                            .httpOnly(true)
                            .path("/")
                            .secure(false) // should be true in production
                            .build();

                    return ResponseEntity.noContent()
                            .header("Set-Cookie", cookie.toString())
                            .<Void>build();
                }
        ).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
