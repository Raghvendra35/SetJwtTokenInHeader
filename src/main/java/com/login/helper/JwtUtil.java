package com.login.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//this class will have method so that token can generate

@Component
public class JwtUtil 
{

	//this is token validity in the form of mili second kitne samay baad token will be expired
		public static final long JWT_TOKEN_VALIDITY = 5 * 100 * 100;

		// every user
		private String SECRET_KEY = "jwtTokenKey";
		
		

		
		//here we'll give token and this method will return username / user
		public String getUsernameFormToken(String token)
		{
			return getClaimFromToken(token, Claims::getSubject);
		}

		
		//we will give token and this method will return of expire date of Token
		public Date getExpirationDateFromToken(String token)
		{
			return getClaimFromToken(token, Claims::getExpiration);
		}
		
		
		//we will give token and this method will return claims
		public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver)
		{
			final Claims claims = getAllClaimsFromToken(token);
			return claimsResolver.apply(claims);
		}
		
		
		//Get AllClaims then use this method
		private Claims getAllClaimsFromToken(String token)
		{
			return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		}
		
		
		
		//check token is expired or not
		private Boolean isTokenExpired(String token)
		{
			final Date expiration = getExpirationDateFromToken(token);
			return expiration.before(new Date());
		}
		
		
		
		
		//this Method will genarate Token
		public String generateToken(UserDetails userDetails)
		{
			Map<String, Object> clailms = new HashMap();
			return doGenerateToken(clailms, userDetails.getUsername());
		 	
		}
		

		//while creating the token
	//1. Define claims of the token, like Issuer, Expiration, subject and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key
	//3. According to JWS Compact serialization://tools.ietf.org/html/draft-itef-jose
	//  compaction of the JWT  to a URL-safe string
		
		// InComplete
		
		private String doGenerateToken(Map<String, Object> claims, String subject)
		{
			return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
			        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000 * 60 * 60 * 10))		
			        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		}
		
		
		
		
		//validate token
		public Boolean validateToken(String token, UserDetails userDetails)
		{
			final String username = getUsernameFormToken(token);
			return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
		}
		
}
