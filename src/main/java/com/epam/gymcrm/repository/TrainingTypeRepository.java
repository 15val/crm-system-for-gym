package com.epam.gymcrm.repository;

import com.epam.gymcrm.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

}
