package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    
    private static final String SECRET_KEY = "ZGNpbmxneA=="; //  密钥
    private static final long EXPRIRE_TIME = 1000 * 60 * 60 * 24; // 过期时间 24小时
    
    /**
     * 生成JWT
     * @param claims JWT中携带的信息
     * @return JWT 字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPRIRE_TIME))
                .compact();
    }
    
    /**
     * 解析JWT
     * @param token JWT字符串
     * @return JWT中携带的信息
     * @throws Exception 解析失败抛出异常 捕获异常处理
     */
    public static Claims parseToken(String token) throws Exception {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new Exception("JWT 解析失败");
        }
    }
    
}
