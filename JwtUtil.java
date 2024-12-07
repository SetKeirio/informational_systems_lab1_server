package informational_systems.lab1;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtil {

    // Секретный ключ для подписи
    private static final String SECRET_KEY = "your-secret-key";

    // Генерация токена
    public static String generateToken(String username) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + username + "\"}";

        String base64Header = encodeBase64UrlSafe(header);
        String base64Payload = encodeBase64UrlSafe(payload);

        String signature = generateSignature(base64Header + "." + base64Payload, SECRET_KEY);

        return base64Header + "." + base64Payload + "." + signature;
    }

    // Валидация токена
    public static boolean validateToken(String token, String username) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        // Восстановить подпись
        String expectedSignature = generateSignature(header + "." + payload, SECRET_KEY);

        // Проверить, совпадает ли подпись
        return expectedSignature.equals(signature) && username.equals(decodeBase64UrlSafe(payload));
    }

    // Генерация подписи для токена
    private static String generateSignature(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encodeBase64UrlSafe(rawHmac.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA-256 signature", e);
        }
    }

    // Кодирование в Base64 URL-safe
    private static String encodeBase64UrlSafe(String str) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    // Декодирование из Base64 URL-safe
    private static String decodeBase64UrlSafe(String base64Str) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(base64Str);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
