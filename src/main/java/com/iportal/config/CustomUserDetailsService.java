package com.iportal.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iportal.entity.User;
import com.iportal.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username not exists"));
		
		if ("RESIGNED".equals(user.getStatus())) {
            throw new DisabledException("Tài khoản của bạn đã bị vô hiệu hóa.");
      }
		
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());

        return new org.springframework.security.core.userdetails.User(
        		username,
                user.getPassword(),
                Collections.singleton(authority) 
        );
	}

}
