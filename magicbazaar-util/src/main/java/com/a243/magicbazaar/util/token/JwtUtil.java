package com.a243.magicbazaar.util.token;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String jwtToken = "magicbazaar@top.com";

    public static String createToken(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken)
                .setClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24L * 60 * 60 * 60 * 1000));
        return jwtBuilder.compact();
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt jwt = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) jwt.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
