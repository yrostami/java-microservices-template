package com.company.template.listener.dummyevent;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.company.template.exception.ResourceInvalidStateException;
import com.company.template.exception.ResourceNotFoundException;
import com.company.template.listener.dummyevent.mapper.DummyEventListenerBeanMapper;
import com.company.template.service.sample.SampleService;
import com.company.template.spec.event.DummyEvent;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
public class DummyEventListener {

	private final SampleService sampleService;

	private final DummyEventListenerBeanMapper mapper;

    // Todo: change the retry policy. configure retry for listener container factory
	@RetryableTopic(include = { Exception.class }, attempts = "1", backOff = @BackOff(delay = 500, multiplier = 2),
			topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
			dltTopicSuffix = "-dlt-${spring.application.name}", retryTopicSuffix = "-retry-${spring.application.name}")

	@KafkaListener(topics = "${dummy-event.topic}")
	public void listen(@Payload @Valid DummyEvent event) {
		log.info("a DummyEvent received: {}", event);
		try {
			sampleService.processEvent(mapper.toDummyModel(event));
		}
		catch (ResourceNotFoundException ex) {
			log.error("resource not found for DummyEvent with trackingCode {}, event will be ignored.",
					event.getTrackingCode());
		}
		catch (ResourceInvalidStateException ex) {
			log.error("resource status is invalid for DummyEvent with trackingCode {}, event will be ignored.",
					event.getTrackingCode());
		}
		catch (Exception ex) {
			log.error("exception occurred while processing DummyEvent with trackingCode {}", event.getTrackingCode(),
					ex);
			throw ex;
		}
	}

}
