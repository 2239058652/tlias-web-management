package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GenJwtTest {

    /**
     * 生成JWT 令牌
     */
    @Test
    public void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "dcinlgx");
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "ZGNpbmxneA==") //  设置密钥
                .addClaims(dataMap)  // 添加自定义数据
                .setExpiration(new Date(System.currentTimeMillis() + + 12 * 3600 * 1000)) // 设置过期时间
                .compact(); // 生成jwt
        System.out.println(jwt);
    }

    /**
     * 解析JWT令牌
     */
    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJkY2lubGd4IiwiZXhwIjoxNzQ4MDk5MzQ3fQ.6x9Ym1296zBZ5MwckBFT9KFXtcbKm1cGzB26jTH3g3M";
        Claims claims = Jwts.parser()
                .setSigningKey("ZGNpbmxneA==")
                .parseClaimsJws(token)
                .getBody();

        System.out.println(claims);
    }
}
