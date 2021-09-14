package edu.strauteka.example.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserService {

    private final PasswordEncoder passwordEncoder;

    private final AppUserRepository appUserRepository;

    private final AppRoleRepository appRoleRepository;

    public Flux<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public Mono<AppUser> findById(Long id) {
        return appUserRepository.findById(id);
    }

    public Mono<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Mono<AppUser> save(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Transactional
    public Mono<AppRole> addRole(AppRole role) {
        return appUserRepository.findById(role.getUserId())
                //or catch db exception
                .switchIfEmpty(Mono.error(new UserNotFoundException(role.getUserId())))
                .flatMap(user -> appRoleRepository.save(role));
    }

    @Transactional
    public Mono<Void> deleteRole(AppRole role) {
        return appRoleRepository.findByUserAndRole(role.getUserId(), role.getRoleId())
                .switchIfEmpty(Mono.error(new UserRoleNotFoundException(role.getUserId())))
                .flatMap(appRoleRepository::delete);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found!")
    private static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long user) {
            super("User with id: " + user + " Not found!");
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User Role not found!")
    private static class UserRoleNotFoundException extends RuntimeException {
        public UserRoleNotFoundException(Long user) {
            super("User with id: " + user + " Not found!");
        }
    }
}
