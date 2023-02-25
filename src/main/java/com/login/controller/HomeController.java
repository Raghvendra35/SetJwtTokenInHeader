
package com.login.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController 
{

	@RequestMapping("/welcome")
	public String welcome()
	{
		 String text="this is private page";
	     text+="this page is not allow unauthrization person";
	
	     return text;
	}
}
