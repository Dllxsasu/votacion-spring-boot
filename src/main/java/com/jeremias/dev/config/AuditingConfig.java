package com.jeremias.dev.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jeremias.dev.security.UserPrincipal;

@Configuration

public class AuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }	
}
class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
    	//Here we obtain the current User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //we return empty if don't exist
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        //obtenemos el principal que en este caso es nuestro User principal el cual esta implementado de userDetails
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        //De ahi retornamos el Id 
        return Optional.ofNullable(userPrincipal.getId());
    }
    
}