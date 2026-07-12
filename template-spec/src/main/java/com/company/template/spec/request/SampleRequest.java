package com.company.template.spec.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SampleRequest extends BaseRequest {

	@NotBlank
	private String name;

}
