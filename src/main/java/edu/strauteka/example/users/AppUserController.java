package edu.strauteka.example.users;

import edu.strauteka.example.dto.UserDto;
import edu.strauteka.example.security.configuration.ApplicationUserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("users")
    Flux<AppUser> findAll() {
        return appUserService.findAll();
    }

    @GetMapping("users/{id}")
    Mono<AppUser> findAll(@PathVariable("id") Long id) {
        return appUserService.findById(id);
    }

    @PostMapping("/users/save")
    Mono<AppUser> save(@RequestBody UserDto userDto) {
        //todo transform UserDto to AppUser beautiful<Mapstruct?>
       return appUserService.save(new AppUser(userDto.getUsername(), userDto.getPassword()));
    }

    @GetMapping("role")
    Mono<ApplicationUserRole[]> findRoles() {
        return Mono.just(ApplicationUserRole.values());
    }

    @PostMapping("role/save")
    Mono<AppRole> addRoleRole(@RequestBody AppRole role) {
        return appUserService.addRole(role);
    }

    @DeleteMapping("role/delete")
    Mono<Void> deleteRoleRole(@RequestBody AppRole role) {
        return appUserService.deleteRole(role);
    }
}
