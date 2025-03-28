package cupid.member.domain;

import static java.nio.charset.StandardCharsets.UTF_8;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {

    @Column(nullable = false)
    private String hashedPassword;

    public Password(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public static Password hashPassword(String password) {
        return new Password(hash(password));
    }

    private static String hash(String password) {
        try {
            // MessageDigest is not thread safe
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean checkPassword(String plainPassword) {
        return this.hashedPassword.equals(hash(plainPassword));
    }
}
