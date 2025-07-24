package org.beni.gestionboisson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/*
* @Provider
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

     private final ObjectMapper objectMapper;

    public JacksonContextResolver() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

}

*
* */