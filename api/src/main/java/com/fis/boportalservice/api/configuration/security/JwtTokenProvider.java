package com.fis.boportalservice.api.configuration.security;

import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.exception.ServerSideException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class JwtTokenProvider implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final String appPublic;

    @Getter private final Long tokenExpiresIn;

    public static final String TOKEN_HEADER = "GTW-Authorization";

    public static final String TOKEN_TYPE = "Bearer";

    public JwtTokenProvider(
            @Value("${app.jwt.public}") final String appPublic,
            @Value("${app.jwt.token.expires-in}") final Long tokenExpiresIn) {
        this.appPublic = appPublic;
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public Claims getClaimsFromToken(final String token) {
        log.debug("Parse token: {}", token);
        return parseToken(token).getPayload();
    }

    /**
     * Boolean result of whether token is valid or not.
     *
     * @param token String token
     * @return boolean
     */
    public boolean validateToken(final String token) {
        log.debug("Check valid token: {}", token);
        parseToken(token);
        return !isTokenExpired(token);
    }

    /**
     * Validate token.
     *
     * @param token String
     * @param httpServletRequest HttpServletRequest
     * @return boolean
     */
    public boolean validateToken(final String token, final HttpServletRequest httpServletRequest) {
        try {
            log.debug("Validate token: {}", token);
            boolean isTokenValid = validateToken(token);
            if (!isTokenValid) {
                log.error("[JWT] Token could not found in local cache");
                httpServletRequest.setAttribute("notfound", "Token is not found in cache");
            }
            return isTokenValid;
        } catch (UnsupportedJwtException e) {
            log.error("[JWT] Unsupported JWT token!");
            httpServletRequest.setAttribute("unsupported", "Unsupported JWT token!");
        } catch (MalformedJwtException e) {
            log.error("[JWT] Invalid JWT token!");
            httpServletRequest.setAttribute("invalid", "Invalid JWT token!");
        } catch (ExpiredJwtException e) {
            log.error("[JWT] Expired JWT token!");
            httpServletRequest.setAttribute("expired", "Expired JWT token!");
        } catch (IllegalArgumentException e) {
            log.error("[JWT] Jwt claims string is empty");
            httpServletRequest.setAttribute("illegal", "JWT claims string is empty.");
        }

        return false;
    }

    /**
     * Extract jwt from bearer string.
     *
     * @param bearer String
     * @return String value of bearer token or null
     */
    public String extractJwtFromBearerString(final String bearer) {
        log.debug("Extract jwt from bearer: {}", bearer);
        if (StringUtils.hasText(bearer) && bearer.startsWith(String.format("%s ", TOKEN_TYPE))) {
            return bearer.substring(TOKEN_TYPE.length() + 1);
        }
        return null;
    }

    /**
     * Extract jwt from request.
     *
     * @param request HttpServletRequest object to get Authorization header
     * @return String value of bearer token or null
     */
    public String extractJwtFromRequest(final HttpServletRequest request) {
        return extractJwtFromBearerString(request.getHeader(TOKEN_HEADER));
    }

    /**
     * Parsing token.
     *
     * @param token String jwt token to parse
     * @return Jws object
     */
    private Jws<Claims> parseToken(final String token) {
        try {
            log.debug("Parsing token: {}", token);
            return Jwts.parser().verifyWith(getSigningKeyEC()).build().parseSignedClaims(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("Token parsing failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Check token is expired or not.
     *
     * @param token String jwt token to get expiration date
     * @return True or False
     */
    private boolean isTokenExpired(final String token) {
        log.debug("Check token is expired or not: {}", token);
        return parseToken(token).getPayload().getExpiration().before(new Date());
    }

    private PublicKey getSigningKeyEC() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            byte[] publicKeyBytes = Base64.getDecoder().decode(appPublic);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new ServerSideException(
                    ErrorCode.RESPONSE_ERROR.getCode(), "Failed to load public key");
        }
    }
}
