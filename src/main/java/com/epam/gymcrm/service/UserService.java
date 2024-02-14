package com.epam.gymcrm.service;

import com.epam.gymcrm.model.User;
import com.epam.gymcrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Predicate;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User createUser() {
		User user = new User();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
		user.setPassword(generatePassword());
		user.setActive(true);
		return user;
	}

	public User updateUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + id));

		user.setActive(!user.isActive());

		return user;
	}

	public Long deleteUser(Long userId) {
		return userId;
	}

	private String generateUsername(String firstName, String lastName) {
		Predicate<String> usernameExists = userRepository::existsByUsername;
		String username = firstName + "." + lastName;
		int serialNumber = 1;
		String finalUsername = username;

		while (usernameExists.test(finalUsername)) {
			finalUsername = username + serialNumber;
			serialNumber++;
		}
		return finalUsername;
	}

	private String generatePassword() {
		String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int index = random.nextInt(symbols.length());
			password.append(symbols.charAt(index));
		}
		return password.toString();
	}
}
