package com.epam.gymcrm.repository;

import com.epam.gymcrm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);

}
