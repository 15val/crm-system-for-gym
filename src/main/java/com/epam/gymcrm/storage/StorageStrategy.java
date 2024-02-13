package com.epam.gymcrm.storage;

public interface StorageStrategy<T> {
	void save(Long id, T data);
	T findById(Long id);
	void update(Long id, T data);
	void delete(Long id);
}