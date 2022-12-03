package com.mmacedoaraujo.supportportal.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mmacedoaraujo.supportportal.constant.SecurityConstant;
import com.mmacedoaraujo.supportportal.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;

import static java.time.Instant.now;

public class JWTTokenProvider {
    @Value("jwt.secret")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(SecurityConstant.MMACEDOARAUJO_LLC)
                .withAudience(SecurityConstant.MMACEDOARAUJO_ADMINISTRATION)
                .withIssuedAt(now())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.AUTHORITIES, claims)
                .withExpiresAt(now().plusMillis(SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {

    }
}
