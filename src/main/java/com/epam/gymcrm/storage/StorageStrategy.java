package com.epam.gymcrm.storage;

public interface StorageStrategy<T> {
	void save(Long id, T data);
	T findById(Long id, Class<T> classType);
	void update(Long id, T data);
	void delete(Long id, Class<T> classType);
}