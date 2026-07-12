package com.company.template.entity.sample;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import com.company.template.entity.BaseJPAEntity;

@Data
@Entity
@Table(name = "samples")
@EqualsAndHashCode(callSuper = true)
public class SampleEntity extends BaseJPAEntity {

	private String name;

	private String trackingCode;

	@Setter(AccessLevel.NONE)
	private Integer status;

	public SampleEntityStatus getStatus() {
		return (this.status != null) ? SampleEntityStatus.fromValue(this.status) : null;
	}

	public void setStatus(SampleEntityStatus status) {
		if (status != null) {
			this.status = status.getValue();
		}
	}

}
