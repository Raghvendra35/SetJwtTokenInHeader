package com.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.login.helper.JwtUtil;
import com.login.model.JwtRequest;
import com.login.model.JwtResponse;
import com.login.service.CustomUserDetailsService;

@RestController
public class JwtController 
{
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		System.out.println(jwtRequest);
	    try
	    {
	    	this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),jwtRequest.getPassWord()));
	    	
	    }catch(UsernameNotFoundException e)
	    {
	    	e.printStackTrace();
	    	throw new Exception("Bad Credentials");
	    }
		
    UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUserName());
	
    String token=this.jwtUtil.generateToken(userDetails);
    System.out.println(token);
    //key as token and value
    
    return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	//here Token has been completed.
	
}
