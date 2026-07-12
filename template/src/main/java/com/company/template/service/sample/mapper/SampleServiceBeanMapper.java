package com.company.template.service.sample.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.company.template.entity.sample.SampleEntity;
import com.company.template.service.sample.model.SampleModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SampleServiceBeanMapper {

	SampleModel toSampleModel(SampleEntity entity);

	SampleEntity toSampleEntity(SampleModel model);

}
