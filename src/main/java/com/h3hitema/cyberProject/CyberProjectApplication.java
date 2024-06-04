package com.h3hitema.cyberProject;

import com.h3hitema.cyberProject.repository.UserRepository;
import com.h3hitema.cyberProject.model.UserEntity;
import io.github.cdimascio.dotenv.Dotenv;
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
			Dotenv dotenv = Dotenv.load();
			UserEntity manager = new UserEntity();
			manager.setUsername(dotenv.get("U_USERNAME"));
			manager.setPassword(passwordEncoder.encode(dotenv.get("U_PASSWORD")));
			manager.setRoles(dotenv.get("U_ROLES"));

			UserEntity admin = new UserEntity();
			admin.setUsername(dotenv.get("A_USERNAME"));
			admin.setPassword(passwordEncoder.encode(dotenv.get("A_PASSWORD")));
			admin.setRoles(dotenv.get("A_ROLES"));
			userRepository.saveAll(List.of(manager,admin));
		};
	}
}
