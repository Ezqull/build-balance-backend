package com.b2.buildbalance.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfiguration {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
