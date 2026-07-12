package com.company.template.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleScheduler {

	@Scheduled(cron = "${sample.scheduled.task.cron}")
	@SchedulerLock(name = "sampleScheduledTask", lockAtMostFor = "15m", lockAtLeastFor = "10m")
	public void mySampleScheduledTask() {

		// Todo: write a sample scheduled task
	}

}
