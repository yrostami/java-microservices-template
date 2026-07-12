package com.company.template.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode
public class BaseJPAEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(name = "creation_date")
	protected Instant creationDate;

	@Column(name = "last_modification_date")
	protected Instant lastModifiedDate;

	@PrePersist
	private void prePersist() {
		this.creationDate = Instant.now();
		this.lastModifiedDate = Instant.now();
	}

	@PreUpdate
	private void preUpdate() {
		this.lastModifiedDate = Instant.now();
	}

}
