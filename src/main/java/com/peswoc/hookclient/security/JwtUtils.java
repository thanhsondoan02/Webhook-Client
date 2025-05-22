package com.peswoc.hookclient.security;

import com.peswoc.hookclient.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

  private final String jwtSecret;
  private final long jwtExpiration;

  public JwtUtils(final JwtConfig jwtConfig) {
    this.jwtSecret = jwtConfig.getSecret();
    this.jwtExpiration = jwtConfig.getExpiration();
  }

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
      .signWith(getSigningKey(jwtSecret), SignatureAlgorithm.HS512)
      .compact();
  }

  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts
      .parserBuilder()
      .setSigningKey(getSigningKey(jwtSecret))
      .build()
      .parseClaimsJws(token)
      .getBody();

    return claims.getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey(jwtSecret))
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Key getSigningKey(String jwtSecret) {
    return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
  }
}

