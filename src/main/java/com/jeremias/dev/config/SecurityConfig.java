package com.jeremias.dev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jeremias.dev.security.JwtAuthenticationEntryPoint;
import com.jeremias.dev.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
//added below line after UserDetailsService() method[
@RequiredArgsConstructor
public class SecurityConfig {
	
;

	    @Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;
	    private final AuthenticationProvider authenticationProvider;
	    @Bean
	    public JwtAuthenticationFilter jwtAuthenticationFilter() {
	        return new JwtAuthenticationFilter();
	    }
	    
	    @Bean
		  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			 
	    	http
            .cors()
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/",
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
                    .permitAll()
                .antMatchers("/api/auth/**")
                    .permitAll()
                .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
                    .authenticationProvider(authenticationProvider);
	        
	     
	        
	     // Add JWT token filter or whatever yout filter
	    	// Add our custom JWT security filter
	        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


		    return http.build();
		  }
	    
	
	
	 
}
