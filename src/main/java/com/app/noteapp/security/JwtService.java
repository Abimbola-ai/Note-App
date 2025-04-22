package com.app.noteapp.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.noteapp.provider.ResourceProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtService {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ResourceProvider provider;

	// Method to generate a JWT token for a given username with
	// an accompanying expiration time
	public String generateJwtToken(String username, long expiration) {
		return JWT.create().withIssuer(this.provider.getJwtIssuer()).withAudience(this.provider.getJwtAudience())
				.withIssuedAt(new Date()).withSubject(username)
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.sign(HMAC512(this.provider.getJwtSecret()));

	}

	// Method that verifies a JWT token input authenticity by using the
	// secret key stored in the ResourceProvider bean
	public DecodedJWT verifyJwtToken(String token) {
		return JWT.require(HMAC512(this.provider.getJwtSecret())).withIssuer(this.provider.getJwtIssuer()).build()
				.verify(token);
	}

	// Method to extract the subject claim from the token
	public String getSubject(String token) {
		return JWT.require(HMAC512(this.provider.getJwtSecret())).withIssuer(this.provider.getJwtIssuer()).build()
				.verify(token).getSubject();
	}

}
