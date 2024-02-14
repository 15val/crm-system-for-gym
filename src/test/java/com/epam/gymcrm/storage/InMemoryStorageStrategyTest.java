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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
		when(mockUserService.createUser()).thenReturn(new User(1L, null, null, null, null, true)); // assuming User creation returns User with ID 2
		when(mockTraineeService.createTrainee(any(Trainee.class))).thenReturn(trainee);
		when(mockDataFileManager.writeDataToFile(anyMap(), anyString())).thenCallRealMethod();
		//???when(mockDataFileManager.readDataFromFile(anyString())).thenCallRealMethod();
		// Act
		inMemoryStorageStrategy.save(1L, (T) trainee);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(1L, (Class<T>) Trainee.class));
	}

/*	@Test
	public void saveTrainerTest() {
		// Arrange
		Trainer trainer = new Trainer();
		when(mockUserService.createUser()).thenReturn(new User(2L)); // assuming User creation returns User with ID 2
		when(mockTrainerService.createTrainer(any(Trainer.class))).thenReturn(trainer);

		// Act
		inMemoryStorageStrategy.save(2L, trainer);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(2L, Trainer.class));
	}

	@Test
	public void saveTrainingTest() {
		// Arrange
		Training training = new Training();
		when(mockTrainingService.createTraining(any(Training.class))).thenReturn(training);

		// Act
		inMemoryStorageStrategy.save(3L, training);

		// Assert
		assertNotNull(inMemoryStorageStrategy.findById(3L, Training.class));
	}

	@Test
	public void findByIdTraineeTest() {
		// Arrange
		Trainee trainee = new Trainee();
		inMemoryStorageStrategy.save(4L, trainee);

		// Act
		Trainee foundTrainee = inMemoryStorageStrategy.findById(4L, Trainee.class);

		// Assert
		assertNotNull(foundTrainee);
	}

	@Test(expected = NoSuchElementException.class)
	public void findByIdNonExistentObjectTest() {
		// Act
		inMemoryStorageStrategy.findById(999L, Trainer.class);
	}

	@Test
	public void updateTraineeTest() {
		// Arrange
		Trainee trainee = new Trainee();
		inMemoryStorageStrategy.save(5L, trainee);

		// Act
		trainee.setName("Updated Name");
		inMemoryStorageStrategy.update(5L, trainee);

		// Assert
		assertEquals("Updated Name", ((Trainee) inMemoryStorageStrategy.findById(5L, Trainee.class)).getName());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void updateTrainingTest() {
		// Arrange
		Training training = new Training();
		inMemoryStorageStrategy.save(6L, training);

		// Act
		inMemoryStorageStrategy.update(6L, training);
	}

	@Test
	public void deleteTraineeTest() {
		// Arrange
		Trainee trainee = new Trainee();
		inMemoryStorageStrategy.save(7L, trainee);

		// Act
		inMemoryStorageStrategy.delete(7L, Trainee.class);

		// Assert
		assertEquals(0, inMemoryStorageStrategy.findById(7L, Trainee.class).size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void deleteTrainerTest() {
		// Arrange
		Trainer trainer = new Trainer();
		inMemoryStorageStrategy.save(8L, trainer);

		// Act
		inMemoryStorageStrategy.delete(8L, Trainer.class);
	}*/
}