package com.nicat.rolebasedaccesscontrol.util;

import com.nicat.rolebasedaccesscontrol.dao.entity.Token;
import com.nicat.rolebasedaccesscontrol.dao.entity.User;
import com.nicat.rolebasedaccesscontrol.dao.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtil {
    private final TokenRepository tokenRepository;

    @Value("${spring.application.security.jwt.secret-key}")
    String secretKey;
    @Value("${spring.application.security.jwt.expiration}")
    Long jwtExpirationTime;
    @Value("${spring.application.security.jwt.refresh-token.expiration}")
    Long refreshExpirationTime;

    public String generateAccessToken(User user) {
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", roles);
        claims.put("userId", user.getId());
        return buildToken(claims, user, jwtExpirationTime);
    }


    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshExpirationTime);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, Long expiration) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // Return claims even for expired tokens
            return e.getClaims();
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = getClaims(token);
        return claimResolver.apply(claims);
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, User user) {
        final String userEmail = extractUsername(token);
        return userEmail.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date(System.currentTimeMillis()));
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public void saveUserToken(User user, String accessToken, String refreshToken) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllTokensOfUser(User user) {
        List<Token> tokens = tokenRepository.findByUserAndIsLoggedOut(user, Boolean.FALSE);
        tokens.forEach(token -> token.setIsLoggedOut(Boolean.TRUE));
        tokenRepository.saveAll(tokens);
    }
}