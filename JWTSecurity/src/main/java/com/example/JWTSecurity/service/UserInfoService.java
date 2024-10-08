package com.example.JWTSecurity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.JWTSecurity.entity.UserInfo;
import com.example.JWTSecurity.repository.UserInfoRepository;

@Service
public class UserInfoService implements UserDetailsService {
	@Autowired
	private UserInfoRepository repository;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	JwtService jwtService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	public String addUser(UserInfo userInfo) {
		// Encode password before saving the user
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
		String jwt = jwtService.generateToken(userInfo.getEmail());
		return jwt;
	}
}
