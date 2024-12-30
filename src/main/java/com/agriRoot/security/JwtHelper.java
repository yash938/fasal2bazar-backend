package com.agriRoot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtHelper {
    public static final long TOKEN_VALID = 5 * 6 * 60 * 1000;
    public static final String SECRET_KEY="yashsharmayashyashyashyashyashyashyashyashyashyashyashyashyashyashyashyashyashyashyashyash";

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsTFunction){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsTFunction.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token){
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            log.error("Failed to parse token :{}",e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }

    public boolean isTokeExpired(String token){
        final Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    public String getEmailFromToken(String token){
        return getClaimFromToken(token,claims -> claims.get("email",String.class));
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",userDetails.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID))
                .signWith(SignatureAlgorithm.ES256,SECRET_KEY)
                .compact();
    }

}
