package com.springboot.study.springboot.controller;

import com.springboot.study.springboot.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.coyote.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthController(JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    private boolean authenticateUser(String username, String password) {
        return "test".equals(username) && "test".equals(password);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        if(authenticateUser(username, password)) {
            String token = JwtUtil.generateToken(username);
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);

        //토큰 유효성 검증
        Claims claims = jwtUtil.validateToken(jwtToken);
        String username = claims.getSubject();

        //토큰 만료 시간 추출
        long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();

        //Redis에 토큰 저장(남은 유효기간 동안 블랙리스트에 유지)
        redisTemplate.opsForValue().set(jwtToken, "blacklisted_user", expiration, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("성공적으로 로그아웃 되었습니다.");
    }
}
