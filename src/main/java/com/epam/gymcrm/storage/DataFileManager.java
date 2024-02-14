package com.epam.gymcrm.storage;

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
	private final String filePath;

	public DataFileManager(String filePath) {
		this.filePath = filePath;
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
					T value = (T) parts[1];
					dataMap.put(key, value);
				}
			}
		}
		return dataMap;
	}

	public void writeDataToFile(Map<Long, T> dataMap) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (Map.Entry<Long, T> entry : dataMap.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue());
				writer.newLine();
			}
		}
	}
	public void writeDataToFile(Map<Long, T> dataMap, String notDefaultFilePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(notDefaultFilePath))) {
			for (Map.Entry<Long, T> entry : dataMap.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue());
				writer.newLine();
			}
		}
	}


}