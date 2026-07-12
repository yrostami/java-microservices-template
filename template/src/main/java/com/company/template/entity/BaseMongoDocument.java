package com.company.template.entity;

import lombok.EqualsAndHashCode;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@EqualsAndHashCode
public class BaseMongoDocument {

	@Id
	protected String id;

	@CreatedDate
	protected Long creationDate;

	@LastModifiedDate
	protected Long lastModifiedDate;

}