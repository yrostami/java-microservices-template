package com.company.template.listener.dummyevent;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.company.template.BaseIntegrationTest;
import com.company.template.entity.sample.SampleEntity;
import com.company.template.entity.sample.SampleEntityStatus;
import com.company.template.repository.SampleRepository;
import com.company.template.spec.event.DummyEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class DummyEventListenerIT extends BaseIntegrationTest {

	@Autowired
	private KafkaTemplate<String, DummyEvent> kafkaTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private SampleRepository sampleRepository;

	@Autowired
	private ConsumerFactory<String, DummyEvent> consumerFactory;

	@Test
	public void dummyEventListenerTest_successful() {
		String topic = env.getRequiredProperty("dummy-event.topic");
		String name = "dummy-event-test-name-1";
		String trackingCode = "dummy-event-test-trc-1";

		DummyEvent event = createDummyEvent(name, trackingCode);
		SampleEntity entity = createInprogressSampleEntity(name, trackingCode);
		sampleRepository.save(entity);

		kafkaTemplate.executeInTransaction((kop) -> kop.send(topic, event));
		await().atMost(5, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS).untilAsserted(() -> {
			SampleEntity sample = sampleRepository.findByTrackingCode(trackingCode).orElse(null);
			assertThat(sample).isNotNull();
			assertThat(sample.getStatus()).isEqualTo(SampleEntityStatus.COMPLETED);
		});
	}

	@Test
	public void dummyEventListenerTest_invalidEvent_goesDlt() {
		String topic = env.getRequiredProperty("dummy-event.topic");
		String deadLetterTopic = topic + "-dlt-" + env.getRequiredProperty("spring.application.name");
		String name = "dummy-event-test-name-2";
		String trackingCode = "dummy-event-test-invalid-trc";

		DummyEvent event = createDummyEvent(name, trackingCode);
		event.setDate(null);

		kafkaTemplate.executeInTransaction((kop) -> kop.send(topic, event));
		Consumer<String, DummyEvent> consumer = consumerFactory.createConsumer("listener-test", "test");
		consumer.subscribe(Collections.singletonList(deadLetterTopic));

		await().atMost(5, TimeUnit.SECONDS).pollInterval(100, TimeUnit.MILLISECONDS).untilAsserted(() -> {
			ConsumerRecords<String, DummyEvent> records = consumer.poll(Duration.ofMillis(100));
			assertThat(records.count()).isGreaterThan(0);
			if (records.count() > 0) {
				consumer.commitSync();
			}
		});
		consumer.close();
	}

	private SampleEntity createInprogressSampleEntity(String name, String trackingCode) {
		SampleEntity sample = new SampleEntity();
		sample.setName(name);
		sample.setTrackingCode(trackingCode);
		sample.setStatus(SampleEntityStatus.IN_PROGRESS);
		return sample;
	}

	private DummyEvent createDummyEvent(String name, String trackingCode) {
		DummyEvent event = new DummyEvent();
		event.setName(name);
		event.setTrackingCode(trackingCode);
		event.setDate(Instant.now());
		return event;
	}

}
