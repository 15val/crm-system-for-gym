package com.epam.gymcrm.storage.impl;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import com.epam.gymcrm.service.UserService;
import com.epam.gymcrm.storage.DataFileManager;
import com.epam.gymcrm.storage.StorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class InMemoryStorageStrategy<T> implements StorageStrategy<T> {
	private final Map<Long, T> storage = new HashMap<>();
	private final Map<Long, T> userStorage = new HashMap<>();
	private final DataFileManager<T> dataFileManager;
	Set<Class<?>> allowedTypes;
	private final TraineeService traineeService;
	private final TrainerService trainerService;
	private final TrainingService trainingService;
	private final UserService userService;
	private final String userStorageFilePath = "userStorage.txt";

	@Autowired
	public InMemoryStorageStrategy(DataFileManager<T> dataFileManager, Set<Class<?>> allowedTypes, UserService userService, TrainingService trainingService, TrainerService trainerService, TraineeService traineeService) {
		this.traineeService = traineeService;
		this.trainerService = trainerService;
		this.trainingService = trainingService;
		this.userService = userService;
		this.dataFileManager = dataFileManager;

		try {
			this.storage.putAll(dataFileManager.readDataFromFile());
			this.userStorage.putAll(dataFileManager.readDataFromFile(userStorageFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		allowedTypes.add(Trainer.class);
		allowedTypes.add(Training.class);
		allowedTypes.add(Trainee.class);
		this.allowedTypes = new HashSet<>(allowedTypes);
	}


	@Override
	public void save(Long id, T data) {
		if (!allowedTypes.contains(data.getClass())) {
			throw new IllegalArgumentException("Data type not allowed");
		}
		T result = null;
		User user;
		if (data instanceof Trainee) {
			user = userService.createUser();
			userStorage.put(user.getId(), (T) user);
			((Trainee) data).setUserId(user.getId());
			result = (T) traineeService.createTrainee((Trainee) data);
		} else if (data instanceof Trainer) {
			user = userService.createUser();
			userStorage.put(user.getId(), (T) user);
			((Trainer) data).setUserId(user.getId());
			result = (T) trainerService.createTrainer((Trainer) data);
		} else if (data instanceof Training) {
			result = (T) trainingService.createTraining((Training) data);
		}
		storage.put(id, result);
		try {
			dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
			dataFileManager.writeDataToFile(storage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public T findById(Long id) {
		T data = storage.get(id);
		if (data!=null) {
			if (data instanceof Trainee) {
				return (T) traineeService.getTraineeById(id);
			} else if (data instanceof Trainer) {
				return (T) trainerService.getTrainerById(id);
			} else if (data instanceof Training) {
				return (T) trainingService.getTrainingById(id);
			}
		}
		throw new NoSuchElementException("Object not exists in storage");
	}

	@Override
	public void update(Long id, T data) {
		if (!allowedTypes.contains(data.getClass())) {
			throw new IllegalArgumentException("Data type not allowed");
		}
		try {
			User user;
			T result = null;
			if (data instanceof Trainee) {
				user = userService.updateUser(((Trainee) data).getId());
				userStorage.put(user.getId(), (T) user);
				((Trainee) data).setUserId(user.getId());
				result = (T) traineeService.updateTrainee((Trainee) data);
			} else if (data instanceof Trainer) {
				user = userService.updateUser(((Trainer) data).getId());
				userStorage.put(user.getId(), (T) user);
				((Trainer) data).setUserId(user.getId());
				result = (T) trainerService.updateTrainer((Trainer) data);
			} else if (data instanceof Training) {
				result = (T) trainingService.updateTraining((Training) data);
			}
			storage.put(id, result);
			try {
				dataFileManager.writeDataToFile(storage);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (UnsupportedOperationException e){
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Long id) {
		T data = findById(id);
		if (data == null) {
			throw new NoSuchElementException("Object not exists in storage");
		}
		try {
			if (data instanceof Trainee) {
				userStorage.remove(userService.deleteUser(((Trainee) data).getUserId()));
				id = traineeService.deleteTrainee(id);
			} else if (data instanceof Trainer) {
				id = trainerService.deleteTrainer(id);
			} else if (data instanceof Training) {
				id = trainingService.deleteTraining(id);
			}
			storage.remove(id);
			try {
				dataFileManager.writeDataToFile(storage);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (UnsupportedOperationException e){
			e.printStackTrace();
		}
	}
}

