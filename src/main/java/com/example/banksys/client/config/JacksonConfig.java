package com.example.banksys.client.config;

import com.example.banksys.client.domain.model.NationalId;
import com.example.banksys.client.interfaces.NationalIdDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.JacksonModule;

@Configuration
public class JacksonConfig {

    @Bean
    public JacksonModule nationalIdModule(){
        SimpleModule module = new SimpleModule();
        module.addDeserializer(NationalId.class, new NationalIdDeserializer());
        return module;
    }
}
