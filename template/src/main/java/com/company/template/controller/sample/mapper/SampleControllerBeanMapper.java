package com.company.template.controller.sample.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.company.template.service.sample.model.SampleModel;
import com.company.template.spec.dto.SampleDto;
import com.company.template.spec.request.SampleRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SampleControllerBeanMapper {

	@Mapping(target = "status", source = "status.value")
	SampleDto toSampleResponse(SampleModel model);

	@Mapping(target = "trackingCode", ignore = true)
	@Mapping(target = "status", ignore = true)
	SampleModel toSampleModel(SampleRequest request);

}
