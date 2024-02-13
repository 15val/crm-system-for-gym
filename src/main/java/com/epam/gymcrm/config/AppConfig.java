package com.epam.gymcrm.config;

import com.epam.gymcrm.storage.DataFileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig<T> {
	@Bean
	public DataFileManager<T> dataFileManager() {
		return new DataFileManager<>("dataFile.txt");
	}

}