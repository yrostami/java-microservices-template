package com.company.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.template.entity.sample.SampleEntity;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Integer> {

	Optional<SampleEntity> findByTrackingCode(String trackingCode);

	Optional<SampleEntity> findByName(String name);

}
