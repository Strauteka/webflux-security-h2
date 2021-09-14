package edu.strauteka.example.security.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.strauteka.example.security.configuration.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    DB_ADMIN(1, List.of(READ)),
    ADMIN(2, List.of(READ, WRITE, DELETE)),
    USER(3, List.of(READ)); //default

    public final int id;
    private final Set<ApplicationUserPermission> permission;

    ApplicationUserRole(int id, List<ApplicationUserPermission> permission) {
        this.id = id;
        this.permission = new HashSet<>(permission);
    }

    public static ApplicationUserRole of(String id) {
        try {
            return of(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return of(-1);
        }
    }

    public static ApplicationUserRole of(int id) {
        return Arrays
                .stream(values())
                .filter(en -> en.id == id)
                .findAny()
                .orElseThrow(RoleNotFoundException::new);
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = permission
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.permission))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Role not found")
    private static class RoleNotFoundException extends RuntimeException {
    }
}
