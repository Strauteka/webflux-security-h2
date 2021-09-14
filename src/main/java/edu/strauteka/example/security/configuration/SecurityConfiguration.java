package edu.strauteka.example.security.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Slf4j
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfiguration {
    public static final String API_USER = "/api/user/**";

    @Bean
    protected SecurityWebFilterChain configure(ServerHttpSecurity http,
                                               CustomAuthenticationManager customAuthenticationManager,
                                               CustomAuthenticationConverter customAuthenticationConverter
    ) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(customAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(customAuthenticationConverter);
        //https://stackoverflow.com/questions/56056404/disable-websession-creation-when-using-spring-security-with-spring-webflux
        http.csrf().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.logout().disable();
//        http.requestCache().requestCache(NoOpServerRequestCache.getInstance());

        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.GET,"/api/role").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.POST,"/api/users/save").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.POST,"/api/role/save").permitAll();
        http.authorizeExchange().pathMatchers(HttpMethod.POST,"/api/jwt/**").permitAll();

        http.authorizeExchange().pathMatchers(API_USER)
                .hasAnyRole(ApplicationUserRole.USER.name(),ApplicationUserRole.ADMIN.name())
                .pathMatchers(HttpMethod.GET, API_USER).hasAuthority(ApplicationUserPermission.READ.permission)
                .pathMatchers(HttpMethod.POST, API_USER).hasAuthority(ApplicationUserPermission.WRITE.permission)
                .pathMatchers(HttpMethod.PUT, API_USER).hasAuthority(ApplicationUserPermission.WRITE.permission)
                .pathMatchers(HttpMethod.DELETE, API_USER).hasAuthority(ApplicationUserPermission.DELETE.permission);
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange().anyExchange().authenticated();
        return http.build();
    }
}
