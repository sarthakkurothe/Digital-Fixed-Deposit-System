package com.fdsystem.backend.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@Component
public class JWTUtils {

  @Value("${spring.app.jwtSecret}")
  private String jwtSecret;

  @Value("${spring.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String getJwtFromHeader(HttpServletRequest request){

    String bearerToken = request.getHeader("Authorization");
    if(bearerToken != null && bearerToken.startsWith("bearer ")){
      return bearerToken.substring(7); // here we are taking the token from 7 index to end index
    }

    return null;
  }


  public String generateTokenFromUsername(UserDetails userDetails){
    String username = userDetails.getUsername();
    return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
            .signWith(key())
            .compact();
  }
  private Key key(){
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }


  public String getUsernameFromJwtToken(String token){
    return Jwts.parser().verifyWith((SecretKey) key()).build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
  }



  public boolean isValidJwtToken(String authToken){
    try{
      Jwts.parser().verifyWith((SecretKey)key()).build()
              .parseSignedClaims(authToken);
      return true;
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
    return false;
  }
}
