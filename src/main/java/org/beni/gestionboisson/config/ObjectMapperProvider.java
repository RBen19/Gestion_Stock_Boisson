package org.beni.gestionboisson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperProvider() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // pour LocalDate, Instant, etc.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // dates lisibles
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}