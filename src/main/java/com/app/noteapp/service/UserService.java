package com.app.noteapp.service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.app.noteapp.exception.domain.EmailExistException;
import com.app.noteapp.exception.domain.EmailNotVerifiedException;
import com.app.noteapp.exception.domain.UserNotFoundException;
import com.app.noteapp.exception.domain.UsernameExistException;
import com.app.noteapp.jpa.User;
import com.app.noteapp.provider.ResourceProvider;
import com.app.noteapp.repository.UserRepository;
import com.app.noteapp.security.JwtService;

@Service
public class UserService {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;

	@Autowired
	ResourceProvider provider;

	public List<User> listUsers() {
		return this.userRepository.findAll();
	}

	public Optional<User> findByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	public void createUser(User user) {
		this.userRepository.save(user);
	}

	public User signup(User user) {
		user.setUsername(user.getUsername().toLowerCase());
		user.setEmail(user.getEmail().toLowerCase());
		// Validate user username and email address
		this.validateUsernameAndEmail(user.getUsername(), user.getEmail());

		user.setEmailVerified(false);
		// Encrypt password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user.setCreatedOn(Timestamp.from(Instant.now()));
		this.userRepository.save(user);
		this.emailService.sendVerificationEmail(user);
		return user;
	}

	private void validateUsernameAndEmail(String username, String emailId) {
		this.userRepository.findByUsername(username).ifPresent(u -> {
			throw new UsernameExistException(String.format("Username already exists, %s", u.getUsername()));
		});

		this.userRepository.findByEmail(emailId).ifPresent(u -> {
			throw new EmailExistException(String.format("Email already exists, %s", u.getEmail()));
		});
	}

	public void verifyEmail() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = this.userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(String.format("Username doesn't exist, %s", username)));
		user.setEmailVerified(true);
		this.userRepository.save(user);
	}

	private static User isEmailVerified(User user) {

		if (user.isEmailVerified().equals(false)) {
			throw new EmailNotVerifiedException(String.format("Email requires verification, %s", user.getEmail()));
		}

		return user;
	}

	private Authentication authenticate(String username, String password) {
		return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	public User authenticate(User user) {

		/* Spring Security Authentication. */
		this.authenticate(user.getUsername(), user.getPassword());

		/* Get User from the DB. */
		return this.userRepository.findByUsername(user.getUsername()).map(UserService::isEmailVerified).get();
	}

	public HttpHeaders generateJwtHeader(String username) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, this.jwtService.generateJwtToken(username, this.provider.getJwtExpiration()));

		return headers;
	}

	public void sendResetPasswordEmail(String email) {

		Optional<User> opt = this.userRepository.findByEmail(email);

		if (opt.isPresent()) {
			this.emailService.sendResetPasswordEmail(opt.get());
		} else {
			logger.debug("Email doesn't exist, {}", email);
		}
	}

	public void resetPassword(String password) {
		// Retrieve username of the currently logged in user
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// Find the user, if not found throw exception
		User user = this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(String.format("Username doesn't exist, %s", username)));
		// Set the new password for the user
		user.setPassword(this.passwordEncoder.encode(password));
		// Save updated information of the user
		this.userRepository.save(user);
	}

	public User getUser() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		/* Get User from the DB. */
		return this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(String.format("Username doesn't exist, %s", username)));
	}

	private void updateValue(Supplier<String> getter, Consumer<String> setter) {
		Optional.ofNullable(getter.get())
				// .filter(StringUtils::hasText)
				.map(String::trim).ifPresent(setter);
	}

	private void updatePassword(Supplier<String> getter, Consumer<String> setter) {

		Optional.ofNullable(getter.get()).filter(StringUtils::hasText).map(this.passwordEncoder::encode)
				.ifPresent(setter);
	}

	private User updateUser(User user, User currentUser) {
		this.updateValue(user::getFirstName, currentUser::setFirstName);
		this.updateValue(user::getLastName, currentUser::setLastName);
		this.updateValue(user::getEmail, currentUser::setEmail);
		this.updatePassword(user::getPassword, currentUser::setPassword);
		return this.userRepository.save(currentUser);
	}

	public User updateUser(User user) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		/* Validates the new email if provided */
		this.userRepository.findByEmail(user.getEmail()).filter(u -> !u.getUsername().equals(username)).ifPresent(u -> {
			throw new EmailExistException(String.format("Email already exists, %s", u.getEmail()));
		});

		/* Get and Update User */
		return this.userRepository.findByUsername(username).map(currentUser -> this.updateUser(user, currentUser))
				.orElseThrow(() -> new UserNotFoundException(String.format("Username doesn't exist, %s", username)));
	}

}
