package com.epam.gymcrm.config;

import com.epam.gymcrm.repository.TraineeRepository;
import com.epam.gymcrm.repository.TrainerRepository;
import com.epam.gymcrm.repository.TrainingRepository;
import com.epam.gymcrm.repository.TrainingTypeRepository;
import com.epam.gymcrm.repository.UserRepository;
import com.epam.gymcrm.service.TraineeService;
import com.epam.gymcrm.service.TrainerService;
import com.epam.gymcrm.service.TrainingService;
import com.epam.gymcrm.service.TrainingTypeService;
import com.epam.gymcrm.service.UserService;
import com.epam.gymcrm.storage.DataFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan(basePackages = "com.epam.gymcrm")
public class AppConfig<T> {
	private TraineeRepository traineeRepository;
	private TrainerRepository trainerRepository;
	private TrainingRepository trainingRepository;
	private TrainingTypeRepository trainingTypeRepository;
	private UserRepository userRepository;

	@Bean
	public DataFileManager<T> dataFileManager() {
		return new DataFileManager<>("dataFile.txt");
	}

	@Bean
	public TraineeService traineeService(){
		return new TraineeService(traineeRepository);
	}

	@Bean
	public TrainerService trainerService(){
		return new TrainerService(trainerRepository);
	}

	@Bean
	public TrainingService trainingService(){
		return new TrainingService(trainingRepository);
	}

	@Bean
	public UserService userService(){
		return new UserService(userRepository);
	}

	@Bean
	public TrainingTypeService trainingTypeService(){
		return new TrainingTypeService(trainingTypeRepository);
	}
}