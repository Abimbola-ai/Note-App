package com.app.noteapp.controller;

import static org.springframework.http.HttpStatus.OK;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.noteapp.jpa.User;
import com.app.noteapp.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/user")
public class UserController {

	// Used to print out the Controller's activity information to the console
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@GetMapping("/")
	public List<User> listUsers() {
		logger.debug("List of application users returned");
		return this.userService.listUsers();
	}

	@GetMapping("/{username}")
	public Optional<User> findByUsername(@PathVariable String username) {
		logger.debug("The method findByUsername was evoked");
		return this.userService.findByUsername(username);
	}

	@PostMapping("/{firstname}/{lastname}/{username}/{password}/{email}")
	public String createUser(@PathVariable String firstname, @PathVariable String lastname,
			@PathVariable String username, @PathVariable String password, @PathVariable String email) {
		User user = new User();

		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setEmailVerified(false);
		user.setCreatedOn(Timestamp.from(Instant.now()));
		logger.debug("New user created with user: {}", user.toString());

		this.userService.createUser(user);
		return "User created successfully";
	}

	@PostMapping("/signup")
	public User signUp(@RequestBody User user) {
		logger.debug("Signing up user with username: {}", user.getUsername());
		return this.userService.signup(user);
	}

	@GetMapping("/verify/email")
	public void verifyEmail() {
		logger.debug("Verifying email");
		this.userService.verifyEmail();

	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		logger.debug("Authenticating, username: {}, password: {}", user.getUsername(), user.getPassword());

		/* Spring Security Authentication. */
		user = this.userService.authenticate(user);

		/* Generate JWT and HTTP Header */
		HttpHeaders jwtHeader = this.userService.generateJwtHeader(user.getUsername());

		logger.debug("User Authenticated, username: {}", user.getUsername());

		return new ResponseEntity<>(user, jwtHeader, OK);
	}

	@GetMapping("/reset/{emailId}")
	public void sendResetPasswordEmail(@PathVariable String emailId) {

		logger.debug("Sending Reset Password Email, emailId: {}", emailId);

		this.userService.sendResetPasswordEmail(emailId);
	}

	@PostMapping("/reset")
	public void passwordReset(@RequestBody JsonNode json) {
		logger.debug("Resetting Password, password: {}", json.get("password").asText());
		// Call the resetPassword from userService
		this.userService.resetPassword(json.get("password").asText());

	}

	@GetMapping("/get")
	public User getUser() {

		logger.debug("Getting User Data");

		return this.userService.getUser();
	}

	@PostMapping("/update")
	public User updateUser(@RequestBody User user) {

		logger.debug("Updating User Data");

		return this.userService.updateUser(user);
	}

	@GetMapping("/test")
	public String testController() {
		logger.debug("The testController() method was invoked!");
		return "The Note application is up and running";
	}

}
