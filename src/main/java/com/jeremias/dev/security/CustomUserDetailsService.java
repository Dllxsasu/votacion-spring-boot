package com.jeremias.dev.security;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.UserRepository;

import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
	  @Autowired
	    UserRepository userRepository;
	   
	  @Override
	    @Transactional
	    public UserDetails loadUserByUsername(String usernameOrEmail)
	            throws UsernameNotFoundException {
	        // Let people login with either username or email
		  //
		  System.out.println("esta entra aqui" + usernameOrEmail);
		  log.info("ta entrando aqui por load");
	        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
	                .orElseThrow(() -> 
	                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
	        );

	        return UserPrincipal.create(user);
	    }

	    // This method is used by JWTAuthenticationFilter
	    @Transactional
	    public UserDetails loadUserById(Long id) {
	    	  log.info("ta entrando aqui por long id");
	        User user = userRepository.findById(id).orElseThrow(
	            () -> new UsernameNotFoundException("User not found with id : " + id)
	        );

	        return UserPrincipal.create(user);
	    }
}
