package com.company.template.service.sample.model;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DummyEventModel {

	private String name;

	private String trackingCode;

	private Instant date;

}
