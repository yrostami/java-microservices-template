package com.company.template.service.sample.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.company.template.entity.sample.SampleEntity;
import com.company.template.entity.sample.SampleEntityStatus;
import com.company.template.exception.DuplicatedRequestException;
import com.company.template.exception.ResourceInvalidStateException;
import com.company.template.exception.ResourceNotFoundException;
import com.company.template.repository.SampleRepository;
import com.company.template.service.sample.SampleService;
import com.company.template.service.sample.mapper.SampleServiceBeanMapper;
import com.company.template.service.sample.model.DummyEventModel;
import com.company.template.service.sample.model.SampleModel;
import com.company.template.util.IdGenerator;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

	private final SampleRepository sampleRepository;

	private final SampleServiceBeanMapper mapper;

	private final IdGenerator idGenerator;

	@Override
	public SampleModel get(String trackingCode) throws ResourceNotFoundException {
		return mapper.toSampleModel(find(trackingCode));
	}

	@Override
	public SampleModel create(SampleModel model) throws DuplicatedRequestException {
		Optional<SampleEntity> optional = sampleRepository.findByName(model.getName());
		if (optional.isPresent()) {
			String msg = String.format("Sample with name [%s] Already exits.", model.getName());
			log.error(msg);
			throw new DuplicatedRequestException(msg);
		}
		model.setTrackingCode(idGenerator.generate());
		model.setStatus(SampleEntityStatus.INITIATED);
		SampleEntity sampleEntity = sampleRepository.save(mapper.toSampleEntity(model));
		return mapper.toSampleModel(sampleEntity);
	}

	@Override
	public void processEvent(DummyEventModel model) throws ResourceNotFoundException, ResourceInvalidStateException {
		log.info("processing Event with trackingCode {}", model.getTrackingCode());
		SampleEntity sample = find(model.getTrackingCode());
		if (sample.getStatus() != SampleEntityStatus.IN_PROGRESS) {
			String logMsg = String.format("Sample with trackingCode %s status is not in-progress. sample status: %s",
					model.getTrackingCode(), sample.getStatus());
			log.error(logMsg);
			throw new ResourceInvalidStateException(logMsg);
		}
		sample.setStatus(SampleEntityStatus.COMPLETED);
		sampleRepository.save(sample);
	}

	private SampleEntity find(String trackingCode) throws ResourceNotFoundException {
		return sampleRepository.findByTrackingCode(trackingCode)
			.orElseThrow(() -> ResourceNotFoundException.from(SampleEntity.class, trackingCode));
	}

}
