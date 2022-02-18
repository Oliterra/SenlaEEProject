package edu.senla.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.senla.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public abstract class AbstractService {

    protected ObjectMapper objectMapper;
    protected ModelMapper modelMapper;
    protected ValidationService validationService;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public final void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }
}
