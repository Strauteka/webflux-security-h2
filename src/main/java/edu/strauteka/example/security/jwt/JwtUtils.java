package edu.strauteka.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.strauteka.example.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class JwtUtils {

    public final static String JWT_COOKIE_NAME = "X-Auth";

    private final Algorithm algorithm;

    private static final String AUTHORITIES_NAME = "authorities";

    public static final Integer REFRESH_EXPIRATION_TIME = 30 * 60 * 1000;
    public static final Integer ACCESS_EXPIRATION_TIME = 5 * 60 * 1000;

    public String createAccessToken(AppUserDetails user, String issuer, Integer expirationTime) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(issuer)
                .withClaim(AUTHORITIES_NAME, user.getPlainAuthorities())
                .sign(algorithm);
    }

    public AppUserDetails createUserDetailsFromToken(String token) {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            final String username = decodedJWT.getSubject();
            String authorities = decodedJWT.getClaim(AUTHORITIES_NAME).asString();
            return new AppUserDetails(username, null, authorities);
    }
}
