package com.app.noteapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	// Used to print out the Controller's activity information to the console
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/test")
	public String testController() {
		logger.debug("The testController() method was invoked!");
		return "The Note application is up and running";
	}

}
