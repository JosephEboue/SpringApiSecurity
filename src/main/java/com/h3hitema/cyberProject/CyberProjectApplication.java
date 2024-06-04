package com.h3hitema.cyberProject;

import com.h3hitema.cyberProject.repository.UserRepository;
import com.h3hitema.cyberProject.model.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class CyberProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(CyberProjectApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			UserEntity manager = new UserEntity();
			manager.setUsername("user");
			manager.setPassword(passwordEncoder.encode("Password0"));
			manager.setRoles("ROLE_USER");

			UserEntity admin = new UserEntity();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("Admin@123"));
			admin.setRoles("ROLE_ADMIN");
			userRepository.saveAll(List.of(manager,admin));
		};
	}
}
