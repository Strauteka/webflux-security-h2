package edu.strauteka.example.users;

import edu.strauteka.example.security.configuration.ApplicationUserRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import static edu.strauteka.example.users.AppRole.APP_ROLE_TABLE;

@Data
@Table(APP_ROLE_TABLE)
public class AppRole {
    public static final String APP_ROLE_TABLE = "APP_USER_ROLES";
    public static final String APP_ROLE_TABLE_ID = "id";
    public static final String APP_ROLE_TABLE_USER_ID = "user_id";
    public static final String APP_ROLE_TABLE_ROLE_ID = "role_id";

    @Id
    @Column(APP_ROLE_TABLE_ID)
    private Long id;
    @Column(APP_ROLE_TABLE_USER_ID)
    private Long userId;
    @Column(APP_ROLE_TABLE_ROLE_ID)
    private Integer roleId;
    //Intellij tells its not used, but it IS!
    public void setRoleId(Integer roleId) {
        //throws if role not found
        ApplicationUserRole.of(roleId);
        this.roleId = roleId;
    }
}
