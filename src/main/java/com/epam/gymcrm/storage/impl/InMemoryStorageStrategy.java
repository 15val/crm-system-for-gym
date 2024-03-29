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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@Slf4j
@Component
public class InMemoryStorageStrategy<T> implements StorageStrategy<T> {
	private final Map<Long, T> userStorage = new HashMap<>();
	private final Map<Long, T> trainerStorage = new HashMap<>();
	private final Map<Long, T> trainingStorage = new HashMap<>();
	private final Map<Long, T> traineeStorage = new HashMap<>();
	private final DataFileManager<T> dataFileManager;
	Set<Class<?>> allowedTypes = Set.of(Trainer.class, Training.class, Trainee.class);
	private final TraineeService traineeService;
	private final TrainerService trainerService;
	private final TrainingService trainingService;
	private final UserService userService;
	private final String userStorageFilePath = "userStorage.txt";
	private final String trainerStorageFilePath = "trainerStorage.txt";
	private final String trainingStorageFilePath = "trainingStorage.txt";
	private final String traineeStorageFilePath = "traineeStorage.txt";
	@Autowired
	public InMemoryStorageStrategy(DataFileManager<T> dataFileManager, UserService userService, TrainingService trainingService, TrainerService trainerService, TraineeService traineeService) {
		this.traineeService = traineeService;
		this.trainerService = trainerService;
		this.trainingService = trainingService;
		this.userService = userService;
		this.dataFileManager = dataFileManager;

		try {
			this.userStorage.putAll(dataFileManager.readDataFromFile(userStorageFilePath));
			this.trainerStorage.putAll(dataFileManager.readDataFromFile(trainerStorageFilePath));
			this.trainingStorage.putAll(dataFileManager.readDataFromFile(trainingStorageFilePath));
			this.traineeStorage.putAll(dataFileManager.readDataFromFile(traineeStorageFilePath));
		} catch (IOException e) {
			log.error("Error while initializing storage: {}", e.getMessage());
		}

	}


	@Override
	public void save(Long id, T data) {
		if (!allowedTypes.contains(data.getClass())) {
			log.error("Data type not allowed");
			throw new IllegalArgumentException("Data type not allowed");
		}

		try {
			T result;
			User user;
			if (data instanceof Trainee) {
				user = userService.createUser();
				userStorage.put(user.getId(), (T) user);
				((Trainee) data).setUserId(user.getId());
				result = (T) traineeService.createTrainee((Trainee) data);
				traineeStorage.put(id, result);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
				dataFileManager.writeDataToFile(traineeStorage,traineeStorageFilePath);
			} else if (data instanceof Trainer) {
				user = userService.createUser();
				userStorage.put(user.getId(), (T) user);
				((Trainer) data).setUserId(user.getId());
				result = (T) trainerService.createTrainer((Trainer) data);
				trainerStorage.put(id, result);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
				dataFileManager.writeDataToFile(trainerStorage,trainerStorageFilePath);
			} else if (data instanceof Training) {
				result = (T) trainingService.createTraining((Training) data);
				trainingStorage.put(id, result);
				dataFileManager.writeDataToFile(trainingStorage,trainingStorageFilePath);
			}
		} catch (IOException e) {
			log.error("Error while saving data: {}", e.getMessage());
		}
	}
	@Override
	public T findById(Long id, Class<T> classType) {

		if (classType.equals(Trainee.class)) {

			T data = traineeStorage.get(id);

			if (data != null) {
				return data;
			}
		} else if (classType.equals(Trainer.class)) {
			T data = trainerStorage.get(id);
			if (data != null) {
				return data;
			}
		} else if (classType.equals(Training.class)) {
			T data = trainingStorage.get(id);
			if (data != null) {
				return data;
			}
		}

		throw new NoSuchElementException("Object does not exist in storage");
	}

	@Override
	public void update(Long id, T data) {
		if (!allowedTypes.contains(data.getClass())) {
			log.error("Data type not allowed");
			throw new IllegalArgumentException("Data type not allowed");
		}
		try {
			User user;
			T result;
			if (data instanceof Trainee) {
				user = userService.updateUser(((Trainee) data).getId());
				userStorage.put(user.getId(), (T) user);
				((Trainee) data).setUserId(user.getId());
				result = (T) traineeService.updateTrainee((Trainee) data);
				traineeStorage.put(id, result);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
				dataFileManager.writeDataToFile(traineeStorage, traineeStorageFilePath);
			} else if (data instanceof Trainer) {
				user = userService.updateUser(((Trainer) data).getId());
				userStorage.put(user.getId(), (T) user);
				((Trainer) data).setUserId(user.getId());
				result = (T) trainerService.updateTrainer((Trainer) data);
				trainerStorage.put(id, result);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
				dataFileManager.writeDataToFile(trainerStorage,trainerStorageFilePath);
			} else if (data instanceof Training) {
				throw new UnsupportedOperationException("Training updating is not supported");
			}
		} catch (UnsupportedOperationException | IOException e){
			log.error("Error while updating data: {}", e.getMessage());
		}
	}

	@Override
	public void delete(Long id, Class<T> classType) {
		try {
			if (classType.equals(Trainee.class)) {
				T data = traineeStorage.get(id);
				if (data == null) {
					throw new NoSuchElementException("Object does not exist in storage");
				}
				userStorage.remove(userService.deleteUser(((Trainee) data).getUserId()));
				traineeStorage.remove(id);
				dataFileManager.writeDataToFile(userStorage, userStorageFilePath);
				dataFileManager.writeDataToFile(traineeStorage, traineeStorageFilePath);
			}
			else {
				throw new UnsupportedOperationException(classType.getName() + " deleting is not supported");
			}
		}
		catch (UnsupportedOperationException | IOException e){
			log.error("Error while deleting data: {}", e.getMessage());
		}
	}
}

