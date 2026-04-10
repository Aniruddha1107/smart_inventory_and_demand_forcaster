package com.myproject.smartinventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login", "/auth/register", "/css/**", "/js/**").permitAll()
				.requestMatchers("/admin/**", "/products/add", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/auth/login")
				.defaultSuccessUrl("/dashboard", true)
				.failureUrl("/auth/login?error=true")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/auth/logout")
				.logoutSuccessUrl("/auth/login?logout=true")
				.invalidateHttpSession(true)
				.permitAll()
			);
		return http.build();
	}
}
