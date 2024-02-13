package com.epam.gymcrm.service;

import com.epam.gymcrm.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService {
	private final TrainingTypeRepository trainingTypeRepository;

	@Autowired
	public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
		this.trainingTypeRepository = trainingTypeRepository;
	}
}
