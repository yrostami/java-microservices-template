package com.company.template.listener.dummyevent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.company.template.service.sample.model.DummyEventModel;
import com.company.template.spec.event.DummyEvent;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DummyEventListenerBeanMapper {

	DummyEventModel toDummyModel(DummyEvent event);

}
