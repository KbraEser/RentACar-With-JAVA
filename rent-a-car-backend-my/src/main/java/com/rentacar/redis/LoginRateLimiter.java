package com.rentacar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class LoginRateLimiter {

    private static final String KEY_PREFIX = "login:attempts:";

    private final Optional<RedisStringStore> redisStringStore;
    private final int maxAttempts;
    private final Duration window;

    public LoginRateLimiter(
            @Autowired(required = false) RedisStringStore redisStringStore,
            @Value("${app.login.max-attempts:5}") int maxAttempts,
            @Value("${app.login.window-minutes:15}") int windowMinutes
    ) {
        this.redisStringStore = Optional.ofNullable(redisStringStore);
        this.maxAttempts = maxAttempts;
        this.window = Duration.ofMinutes(windowMinutes);
    }

    public boolean isBlocked(String email) {
        return redisStringStore
                .map(store -> {
                    String count = store.get(key(email));
                    return count != null && Integer.parseInt(count) >= maxAttempts;
                })
                .orElse(false);
    }

    public void recordFailedAttempt(String email) {
        redisStringStore.ifPresent(store -> {
            long attempts = store.increment(key(email));
            if (attempts == 1) {
                store.expire(key(email), window);
            }
        });
    }

    public void clearAttempts(String email) {
        redisStringStore.ifPresent(store -> store.delete(key(email)));
    }

    private String key(String email) {
        return KEY_PREFIX + email.toLowerCase();
    }
}
