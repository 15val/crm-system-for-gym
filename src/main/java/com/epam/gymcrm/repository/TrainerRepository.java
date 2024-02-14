package com.epam.gymcrm.repository;

import com.epam.gymcrm.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
	Trainer findByUserId(Long userId);
}