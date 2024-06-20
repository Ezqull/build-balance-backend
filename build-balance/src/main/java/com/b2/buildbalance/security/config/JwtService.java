package com.b2.buildbalance.security.config;

import com.b2.buildbalance.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final int MINUTE_IN_MILLIS = 60000;

    private final Environment env;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(
            final String token,
            final Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(final UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(
                extraClaims,
                userDetails,
                Long.parseLong(Optional.of(env.getProperty("JWT_EXPIRATION"))
                        .orElseThrow(() -> new NotFoundException("Corrupted environment entry: JWT_EXPIRATION.")
                        ))
        );
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(
                new HashMap<>(),
                userDetails,
                Long.parseLong(Optional.of(env.getProperty("JWT_REFRESH_EXPIRATION"))
                        .orElseThrow(() -> new NotFoundException("Corrupted environment entry: JWT_REFRESH_EXPIRATION.")
                        ))
        );
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (15 * MINUTE_IN_MILLIS)))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(
            final String token,
            final UserDetails userDetails
    ) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        String secret = env.getProperty("JWT_SECRET_KEY");
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
