package com.company.template.controller.sample;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.template.controller.sample.mapper.SampleControllerBeanMapper;
import com.company.template.exception.DuplicatedRequestException;
import com.company.template.exception.ResourceNotFoundException;
import com.company.template.service.sample.SampleService;
import com.company.template.service.sample.model.SampleModel;
import com.company.template.spec.dto.SampleDto;
import com.company.template.spec.request.SampleRequest;
import com.company.template.spec.responce.ControllerResponse;
import com.company.template.util.ResponseUtil;

@Slf4j
@RestController
@RequestMapping("/v1/sample")
@RequiredArgsConstructor
public class SampleController {

	private final SampleService sampleService;

	private final SampleControllerBeanMapper mapper;

	@GetMapping(path = "/{trackingCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ControllerResponse<SampleDto>> getSample(@PathVariable("trackingCode") String trackingCode)
			throws ResourceNotFoundException {
		log.debug("getSample [{}] request received.", trackingCode);
		SampleModel sample = sampleService.get(trackingCode);
		SampleDto response = mapper.toSampleResponse(sample);
		return ResponseEntity.ok(ResponseUtil.successful(response));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ControllerResponse<SampleDto>> createSample(@Valid @RequestBody SampleRequest request)
			throws DuplicatedRequestException {
		log.debug("createSample [{}] request received.", request);
		SampleModel sample = mapper.toSampleModel(request);
		sample = sampleService.create(sample);
		SampleDto response = mapper.toSampleResponse(sample);
		return new ResponseEntity<>(ResponseUtil.successful(response), HttpStatus.CREATED);
	}

}
