package edu.strauteka.example.users;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import static edu.strauteka.example.users.AppUser.APP_USER_TABLE;

@Data
@Table(APP_USER_TABLE)
public class AppUser {
    public static final String APP_USER_TABLE = "APP_USERS";
    public static final String APP_USER_TABLE_ID = "ID";
    public static final String APP_USER_TABLE_USERNAME = "USERNAME";
    public static final String APP_USER_TABLE_PASSWORD = "PASSWORD";
    public static final String APP_USER_TABLE_IS_ACTIVE = "IS_ACTIVE";

    public AppUser(String username, String password) {
        this.username = username;
        this.password= password;
        this.isActive = true;
    }

    @Id
    @Column(APP_USER_TABLE_ID)
    private Long id;

    @Column(APP_USER_TABLE_USERNAME)
    private String username;

    @Column(APP_USER_TABLE_PASSWORD)
    private String password;

    @Column(APP_USER_TABLE_IS_ACTIVE)
    private Boolean isActive;
}
