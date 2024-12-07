package informational_systems.lab1.services;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthentifyService {

    private final String SECRET_KEY = "fdnjdseifenwkofwepewplqekwfsamklfdsndalksdajsjeqwheouwqio31242131231231270378"; // Замените на свой секретный ключ
    private final long VALIDITY_IN_MILLISECONDS = 3600000; // 1 час

    public String generateToken(String username, String role, int userId) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put("userId", userId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            // Проверяем токен с помощью секретного ключа
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Получаем дату истечения срока действия токена
            Date expiration = claims.getExpiration();
            System.out.println("Exp: " + expiration);

            // Проверяем, не истек ли токен
            if (expiration != null && expiration.after(new Date())) {
                System.out.println("Token is valid.");
                return true; // Токен валиден
            } else {
                System.out.println("Token has expired.");
                return false; // Токен истек
            }
        } catch (SignatureException e) {
            System.out.println("Invalid signature.");
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired.");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token.");
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
        }
        return false;
    }
}