package com.login.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.login.helper.JwtUtil;
import com.login.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter 
{

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetails;

    
    
	//it is used validate token
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
      String requestTokenHeader=request.getHeader("Authorization");
	  String username=null;
	  String jwtToken=null;
	  
	  //null and format checking
	  if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
	   {
	  
		   jwtToken=requestTokenHeader.substring(7);
       try
          {
	
        	 //here we are passing token and then we'll get username
	         username=this.jwtUtil.getUsernameFormToken(jwtToken);
	   
	      }catch(Exception e)
	        {
		     e.printStackTrace();
	        }
	    }
	  
	  //Get Details
	  UserDetails userDetails=this.customUserDetails.loadUserByUsername(username);
	  
	  
	  //here we are checking username is null or not
	  //Security
          if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null)
          {
    	  
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
        		                                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        	  
        //Set Details
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        //Set this thing in security Context
        //and authentication
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        
        //Now Token has been authenticated
        
        //now we'll call chain and do filter
          }else
          {  
        	  System.out.println("Token is not validated");
          }
          

          
          
          //here forward request and response
          //if any exception(unAuthorization) comes then request and response will not forward
          filterChain.doFilter(request, response);
       
          //Next thing inside SecurityConfig -> there we'll configure

	  }
	
	
}



























