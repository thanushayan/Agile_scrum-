package com.projectTwo.AS_PMT_UserAuth_Service.Services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectTwo.AS_PMT_UserAuth_Service.Entity.UserCredential;
import com.projectTwo.AS_PMT_UserAuth_Service.Repositories.UserCredentialRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

@Service
public class AuthService {

	@Autowired
	private UserCredentialRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	public UserCredential saveUser(UserCredential credential) {
		credential.setPassword(passwordEncoder.encode(credential.getPassword()));
		repository.save(credential);
		return repository.save(credential);
	}

	public String generateToken(String username) {
		return jwtService.generateToken(username);
	}

	public String validateToken(String token) throws Exception {
	    try {
	        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	        return claims.getSubject();
	    } catch (ExpiredJwtException ex) {
	        // Token has expired
	        // Handle the expired token error, e.g., log the error or throw a custom exception
	        throw new Exception("Token has expired");
	    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
	        // Token is invalid or malformed
	        // Handle the invalid token error, e.g., log the error or throw a custom exception
	        throw new Exception("Invalid token");
	    }
	}


	public ResponseEntity<UserCredential> getUserByUserID(String userID) {
		UserCredential user = repository.findByname(userID);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	public ResponseEntity<UserCredential> getUserByEmail(String email) {
		UserCredential user = repository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	private SecretKey getSignKey() {
		// Your secret key in string format
		String secretKeyString = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

		// Encode the secret key as bytes
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public void deleteUser(String username) {
		UserCredential user = repository.findByname(username);
		if (user != null) {
			repository.delete(user);
		}
		
	}


}
