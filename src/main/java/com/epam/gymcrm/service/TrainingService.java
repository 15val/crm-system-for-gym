package com.epam.gymcrm.service;

import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
	private TrainingRepository trainingRepository;

	@Autowired
	public TrainingService(TrainingRepository trainingRepository) {
		this.trainingRepository = trainingRepository;
	}

	public Training createTraining(Training training) {
		return training;
	}

	public Training getTrainingById(Long trainingId) {
		return trainingRepository.findById(trainingId).orElse(null);
	}

	public Long deleteTraining(Long trainingId) {
		throw new UnsupportedOperationException("Not allowed to delete training");
	}
	public Training updateTraining(Training training) {
		throw new UnsupportedOperationException("Not allowed to update training");
	}
}
