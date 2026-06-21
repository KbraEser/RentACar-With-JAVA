package com.rentacar.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthCookieService {

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final boolean secureCookies;
    private final String sameSite;
    private final long accessTokenMaxAgeSeconds;
    private final long refreshTokenMaxAgeSeconds;

    public AuthCookieService(
            @Value("${app.cookie.secure:false}") boolean secureCookies,
            @Value("${app.cookie.same-site:Lax}") String sameSite,
            @Value("${jwt.access-expiration-ms:900000}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms:604800000}") long refreshExpirationMs
    ) {
        this.secureCookies = secureCookies;
        this.sameSite = sameSite;
        this.accessTokenMaxAgeSeconds = Duration.ofMillis(accessExpirationMs).getSeconds();
        this.refreshTokenMaxAgeSeconds = Duration.ofMillis(refreshExpirationMs).getSeconds();
    }

    public void setAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(ACCESS_TOKEN_COOKIE, accessToken, accessTokenMaxAgeSeconds).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(REFRESH_TOKEN_COOKIE, refreshToken, refreshTokenMaxAgeSeconds).toString());
    }

    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(ACCESS_TOKEN_COOKIE, accessToken, accessTokenMaxAgeSeconds).toString());
    }

    public void clearAuthCookies(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(ACCESS_TOKEN_COOKIE, "", 0).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(REFRESH_TOKEN_COOKIE, "", 0).toString());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        return readCookie(request, ACCESS_TOKEN_COOKIE);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        return readCookie(request, REFRESH_TOKEN_COOKIE);
    }

    private ResponseCookie buildCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secureCookies)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(sameSite)
                .build();
    }

    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
