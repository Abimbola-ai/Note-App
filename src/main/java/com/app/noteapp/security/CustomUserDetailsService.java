package com.app.noteapp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.noteapp.jpa.User;
import com.app.noteapp.repository.UserRepository;

@Service("UserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt = this.userRepository.findByUsername(username);
		if (opt.isEmpty()) {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		return new CustomUserDetails(opt.get());
	}

}
