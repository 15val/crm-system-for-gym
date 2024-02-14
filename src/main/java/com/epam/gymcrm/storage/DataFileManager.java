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
		System.out.println("readDataFromFile entered");
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
				System.out.println("one line read");
			}
		}
		return dataMap;
	}

	public boolean writeDataToFile(Map<Long, T> dataMap, String notDefaultFilePath) throws IOException {
		System.out.println("writeDataToFile entered");
	/*	System.out.println(dataMap.toString());
		System.out.println(notDefaultFilePath);*/
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(notDefaultFilePath))) {
			for (Map.Entry<Long, T> entry : dataMap.entrySet()) {
				String serializedKey = String.valueOf(entry.getKey());
				String serializedValue = String.valueOf(entry.getValue());
				writer.write(serializedKey + "," + serializedValue);
				writer.newLine();
				System.out.println("one line written");
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	public void writeDataToFile(Map<Long, T> dataMap) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (Map.Entry<Long, T> entry : dataMap.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue());
				writer.newLine();
			}
		}
	}
	public Map<Long, T> readDataFromFile() throws IOException {
		Map<Long, T> dataMap = new HashMap<>();
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					long key = Long.parseLong(parts[0]);
					T value = (T) parts[1];
					dataMap.put(key, value);
				}
			}
		}
		return dataMap;
	}
}