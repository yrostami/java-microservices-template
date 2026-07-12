package com.company.template.spec.event;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DummyEvent {

	@NotBlank
	private String name;

	@NotBlank
	private String trackingCode;

	@NotNull
	private Instant date;

}
