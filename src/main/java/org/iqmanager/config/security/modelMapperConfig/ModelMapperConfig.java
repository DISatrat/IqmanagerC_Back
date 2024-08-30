package org.iqmanager.config.security.modelMapperConfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper;
    }
}
