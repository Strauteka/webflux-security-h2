package edu.strauteka.example.security.configuration;

public enum ApplicationUserPermission {
    READ("all:read"),
    WRITE("all:write"),
    DELETE("all:delete");

    public final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
