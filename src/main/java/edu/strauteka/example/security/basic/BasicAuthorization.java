package edu.strauteka.example.security.basic;

import edu.strauteka.example.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
public class BasicAuthorization {
    public final static String BASIC_HEADER_NAME = "Authorization";
    public final static String BASIC_HEADER_PREFIX = "Basic ";

    public UserDto getUser(String base64Login) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Login);
        String decodedString = new String(decodedBytes);
        final String[] userPassword = decodedString.split(":", 2);
        return new UserDto(userPassword[0], userPassword[1]);
    }
}
