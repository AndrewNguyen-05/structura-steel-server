package com.structura.steel.coreservice.utils;

import com.structura.steel.commons.utils.AppConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimiter {

    private final StringRedisTemplate redisTemplate;

    public boolean isEmailLockedOut(String email) {
        String key = "email_attempts:" + email;
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null && Integer.parseInt(attempts) >= AppConstants.MAX_EMAIL_ATTEMPTS;
    }

    public void incrementEmailAttempts(String email) {
        String key = "email_attempts:" + email;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(AppConstants.LOCKOUT_DURATION_MINUTES));
    }

    public void resetEmailAttempts(String email) {
        String key = "email_attempts:" + email;
        redisTemplate.delete(key);
    }

    public boolean isOtpLockedOut(String email) {
        String key = "otp_attempts:" + email;
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null && Integer.parseInt(attempts) >= AppConstants.MAX_OTP_ATTEMPTS;
    }

    public void incrementOtpAttempts(String email) {
        String key = "otp_attempts:" + email;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(AppConstants.LOCKOUT_DURATION_MINUTES));
    }

    public void resetOtpAttempts(String email) {
        String key = "otp_attempts:" + email;
        redisTemplate.delete(key);
    }
}
