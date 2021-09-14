package edu.strauteka.example.security.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
@Slf4j
@Profile("PBKDF2-Encoder")
@Configuration
public class PBKDF2Encoder implements PasswordEncoder {
    public static final String algorithm = "PBKDF2WithHmacSHA512";

    @Value("${password.secret}")
    private String secret;

    @Value("${password.encoder.iteration}")
    private Integer iteration;

    @Value("${password.encoder.key-length}")
    private Integer keyLength;

    @Override
    public String encode(CharSequence charSequence) {
        try {
            byte[] result = SecretKeyFactory.getInstance(algorithm)
                    .generateSecret(new PBEKeySpec(charSequence.toString().toCharArray(),
                            secret.getBytes(),
                            iteration,
                            keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence charSequence, String string) {
        return encode(charSequence).equals(string);
    }
}
