package com.peoplepay.api;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.peoplepay.api.repository.AppUserRepository;

@Configuration
public class SecurityConfig {
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService users(AppUserRepository repository) {
		return username -> repository.findByUsernameWithRole(username)
				.map(u -> User.withUsername(u.getUsername()).password(u.getPasswordHash()).roles(u.getRole().getName())
						.disabled(!u.isEnabled()).build())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/roles", "/h2-console/**")
						.permitAll().requestMatchers(HttpMethod.GET, "/api/countries/**").authenticated()
						.requestMatchers("/api/countries/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/employees/**", "/api/salary-revisions/**")
						.hasAnyRole("ADMIN", "HR").requestMatchers(HttpMethod.PUT, "/api/employees/**")
						.hasAnyRole("ADMIN", "HR").requestMatchers(HttpMethod.PATCH, "/api/employees/**")
						.hasAnyRole("ADMIN", "HR").requestMatchers("/api/audit/**").hasRole("ADMIN")
						.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults())
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		c.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
		c.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
		s.registerCorsConfiguration("/**", c);
		return s;
	}
}
