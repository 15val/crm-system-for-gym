package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.repository.TraineeRepository;
import com.epam.gymcrm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Predicate;

@Service
public class TraineeService {
	private final TraineeRepository traineeRepository;
	private final UserRepository userRepository;

	@Autowired
	public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository) {
		this.traineeRepository = traineeRepository;
		this.userRepository = userRepository;
	}

	public Trainee createTrainee(Trainee trainee) {

		return trainee;
	}

	public Trainee updateTrainee(Trainee trainee) {
		return trainee;
	}

	public Long deleteTrainee(Long traineeId) {
		return traineeId;
	}

	public Trainee getTraineeById(Long traineeId) {
		return traineeRepository.findById(traineeId).orElse(null);
	}

}
