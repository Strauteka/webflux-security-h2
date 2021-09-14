package edu.strauteka.example.users;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AppRoleRepository extends ReactiveCrudRepository<AppRole, Long> {

    @Query("SELECT * FROM " + AppRole.APP_ROLE_TABLE + " r " +
            "WHERE r." + AppRole.APP_ROLE_TABLE_USER_ID + " = :userId " +
            "AND r." + AppRole.APP_ROLE_TABLE_ROLE_ID + " = :RoleId;")
    Mono<AppRole> findByUserAndRole(Long userId, Integer RoleId);
}
