package com.company.template.service.sample.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.company.template.entity.sample.SampleEntityStatus;

@Setter
@Getter
@ToString
public class SampleModel {

	private String name;

	private String trackingCode;

	private SampleEntityStatus status;

}
