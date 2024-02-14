package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
	private final TrainerRepository trainerRepository;

	@Autowired
	public TrainerService(TrainerRepository trainerRepository) {
		this.trainerRepository = trainerRepository;
	}

	public Trainer createTrainer(Trainer trainer) {
		return trainer;
	}

	public Trainer updateTrainer(Trainer trainer) {
		return trainer;
	}

	public Trainer getTrainerById(Long trainerId) {
		return trainerRepository.findById(trainerId).orElse(null);
	}

	public Long deleteTrainer(Long trainerId) {
		throw new UnsupportedOperationException("Not allowed to delete trainer");
	}
}
