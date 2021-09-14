package edu.strauteka.example.security.configuration;

import edu.strauteka.example.security.basic.BasicAuthorization;
import edu.strauteka.example.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtUtils jwtUtils;
    private final BasicAuthorization basicAuthorization;

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                //Cookie JWT
                .map(exchange -> exchange
                        .getRequest()
                        .getCookies()
                        .entrySet()
                        .stream()
                        .filter(cookie -> cookie.getKey().equals(JwtUtils.JWT_COOKIE_NAME))
                        .map(Map.Entry::getValue)
                        .map(value -> value
                                .stream()
                                .map(HttpCookie::getValue)
                                .map(jwtUtils::createUserDetailsFromToken)
                                .findAny()
                                .orElseThrow())
                        .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                                "notUsed", //no password with jwt
                                userDetails.getAuthorities()))
                        .findAny()
                        .orElseThrow()
                ).map(appUserDetails -> (Authentication) appUserDetails)
                .onErrorResume(throwable -> Mono.empty())
                // try base64
                .switchIfEmpty(Mono.justOrEmpty(serverWebExchange).map(exchange ->
                        Objects.requireNonNull(exchange
                                .getRequest()
                                .getHeaders()
                                .get(BasicAuthorization.BASIC_HEADER_NAME))
                                .stream()
                                .filter(headers -> headers.startsWith(BasicAuthorization.BASIC_HEADER_PREFIX))
                                .map(header -> header.substring(BasicAuthorization.BASIC_HEADER_PREFIX.length()))
                                .map(basicAuthorization::getUser)
                                .map(user -> new UsernamePasswordAuthenticationToken(user.getUsername(),
                                        user.getPassword(),
                                        List.of()))
                                .findAny()
                                .orElseThrow()
                ))
                .onErrorResume(throwable -> Mono.empty());
    }
}
