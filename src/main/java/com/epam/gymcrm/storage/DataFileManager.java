package com.epam.gymcrm.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataFileManager<T> {
	private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	private final String filePath;

	public DataFileManager(String filePath) {
		this.filePath = filePath;
	}
	public Map<Long, T> readDataFromFile(String notDefaultFilePath) throws IOException {
		Map<Long, T> dataMap = new HashMap<>();
		File file = new File(notDefaultFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(notDefaultFilePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					long key = Long.parseLong(parts[0]);
					T value = objectMapper.readValue(parts[1], new TypeReference<T>() {});
					dataMap.put(key, value);
				}
			}
		}
		return dataMap;
	}

	public boolean writeDataToFile(Map<Long, T> dataMap, String notDefaultFilePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(notDefaultFilePath))) {
			for (Map.Entry<Long, T> entry : dataMap.entrySet()) {
				String serializedKey = String.valueOf(entry.getKey());
				String serializedValue = String.valueOf(entry.getValue());
				writer.write(serializedKey + "," + serializedValue);
				writer.newLine();
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}