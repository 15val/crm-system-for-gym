package com.epam.gymcrm.storage;

import com.epam.gymcrm.model.Trainee;
import com.epam.gymcrm.model.Trainer;
import com.epam.gymcrm.model.Training;
import com.epam.gymcrm.model.User;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import com.epam.gymcrm.service.UserService;
import com.epam.gymcrm.storage.impl.InMemoryStorageStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryStorageStrategyTest <T> {

	@Mock
	private DataFileManager<T> mockDataFileManager;

	@Mock
	private UserService mockUserService;

	@Mock
	private TraineeService mockTraineeService;

	@Mock
	private TrainerService mockTrainerService;

	@Mock
	private TrainingService mockTrainingService;

	@InjectMocks
	private InMemoryStorageStrategy<T> inMemoryStorageStrategy;

	@Before
	public void setUp() throws IOException {
		Map<Long, T> userStorage = new HashMap<>();
		Map<Long, T> trainerStorage = new HashMap<>();
		Map<Long, T> trainingStorage = new HashMap<>();
		Map<Long, T> traineeStorage = new HashMap<>();
		Set<Class<?>> allowedTypes = Set.of(Trainer.class, Trainee.class, Training.class);
		when(mockDataFileManager.readDataFromFile(anyString())).thenReturn(Collections.emptyMap());

		inMemoryStorageStrategy = new InMemoryStorageStrategy<>(mockDataFileManager, mockUserService, mockTrainingService, mockTrainerService, mockTraineeService);
	}

	@Test
	public void saveTraineeTest() throws IOException {
		// Arrange
		Trainee trainee = new Trainee();
		trainee.setId(1L);
		trainee.setAddress("xxx");
		trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
		trainee.setUserId(1L);
		when(mockUserService.createUser()).thenReturn(new User(1L, null, null, null, null, true));
		when(mockTraineeService.createTrainee(any(Trainee.class))).thenReturn(trainee);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();

		// Act
		inMemoryStorageStrategy.save(1L, (T) trainee);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(1L, (Class<T>) Trainee.class));
	}

	@Test
		public void saveTrainerTest() throws IOException {
			// Arrange
		Trainer trainer = new Trainer();
		trainer.setId(1L);
		trainer.setSpecialization("xxx");
		trainer.setUserId(1L);
		when(mockUserService.createUser()).thenReturn(new User(1L, null, null, null, null, true));
		when(mockTrainerService.createTrainer(any(Trainer.class))).thenReturn(trainer);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();

		// Act
		inMemoryStorageStrategy.save(1L, (T) trainer);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(1L, (Class<T>)Trainer.class));
	}

	@Test
	public void saveTrainingTest() throws IOException {
		// Arrange
		Training training = new Training();
		training.setId(1L);
		training.setTraineeId(1L);
		training.setTrainerId(1L);
		when(mockTrainingService.createTraining(any(Training.class))).thenReturn(training);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();

		// Act
		inMemoryStorageStrategy.save(1L, (T) training);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(1L, (Class<T>) Training.class));
	}

	@Test
	public void findByIdTraineeTest() throws IOException {
		// Arrange
		Trainee trainee = new Trainee();
		trainee.setId(1L);
		trainee.setAddress("xxx");
		trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
		trainee.setUserId(1L);
		when(mockUserService.createUser()).thenReturn(new User(1L, null, null, null, null, true));
		when(mockTraineeService.createTrainee(any(Trainee.class))).thenReturn(trainee);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();

		// Act
		inMemoryStorageStrategy.save(1L, (T) trainee);
		Trainee foundTrainee = (Trainee) inMemoryStorageStrategy.findById(1L, (Class<T>) Trainee.class);

		// Assert
		assertNotNull(foundTrainee);
	}

	@Test(expected = NoSuchElementException.class)
	public void findByIdNonExistentObjectTest() {
		// Act
		inMemoryStorageStrategy.findById(999L, (Class<T>) Trainer.class);
	}

	@Test
	public void updateTraineeTest() throws IOException {
		// Arrange
		Trainee trainee = new Trainee();
		trainee.setId(1L);
		trainee.setAddress("xxx");
		trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
		trainee.setUserId(1L);
		when(mockUserService.updateUser(1L)).thenReturn(new User(1L, null, null, null, null, false));

		when(mockTraineeService.updateTrainee(any(Trainee.class))).thenReturn(trainee);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();

		// Act
		inMemoryStorageStrategy.update(1L, (T) trainee);

		// Assert
		assertEquals(1L, ((Trainee) inMemoryStorageStrategy.findById(1L, (Class<T>) Trainee.class)).getUserId());
	}

	@Test(expected = NoSuchElementException.class)
	public void deleteTraineeTest()  {

		// Act
		inMemoryStorageStrategy.delete(1L, (Class<T>) Trainee.class);

	}


}