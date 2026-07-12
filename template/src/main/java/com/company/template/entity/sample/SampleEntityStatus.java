package com.company.template.entity.sample;

public enum SampleEntityStatus {

	INITIATED(0), IN_PROGRESS(1), COMPLETED(2), FAILED(3);

	private final int value;

	SampleEntityStatus(int value) {
		this.value = value;
	}

	public static SampleEntityStatus fromValue(int value) {
		for (SampleEntityStatus status : SampleEntityStatus.values()) {
			if (status.value == value) {
				return status;
			}
		}
		throw new IllegalArgumentException("invalid value for SampleEntityStatus: " + value);
	}

	public int getValue() {
		return value;
	}

}
