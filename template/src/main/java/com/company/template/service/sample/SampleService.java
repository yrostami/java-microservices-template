package com.company.template.service.sample;

import com.company.template.exception.DuplicatedRequestException;
import com.company.template.exception.ResourceInvalidStateException;
import com.company.template.exception.ResourceNotFoundException;
import com.company.template.service.sample.model.DummyEventModel;
import com.company.template.service.sample.model.SampleModel;

public interface SampleService {

	SampleModel get(String trackingCode) throws ResourceNotFoundException;

	SampleModel create(SampleModel model) throws DuplicatedRequestException;

	void processEvent(DummyEventModel model) throws ResourceNotFoundException, ResourceInvalidStateException;

}
