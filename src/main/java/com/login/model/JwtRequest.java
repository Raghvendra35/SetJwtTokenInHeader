package com.login.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JwtRequest 
{

	String userName;
	String passWord;
	
}
