package com.peoplepay.api;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.peoplepay.api.repository.AppRoleRepository;
import com.peoplepay.api.repository.AppUserRepository;

@Configuration
public class AuthSeedInitializer {
	@Bean
	ApplicationRunner upgradeSeedPasswords(AppUserRepository users, AppRoleRepository roles, PasswordEncoder encoder) {
		return args -> {
			for (var user : users.findAll()) {
				String value = user.getPasswordHash();
				if (value.startsWith("{seed}")) {
					user.setPasswordHash(encoder.encode(value.substring(6)));
					users.saveAndFlush(user);
				}
			}
		};
	}
}
