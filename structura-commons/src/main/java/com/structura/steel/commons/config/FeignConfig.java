package com.structura.steel.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.structura.steel.commons.exception.ResourceNotBelongToException;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    private final ObjectMapper mapper;

    public FeignConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            private final ErrorDecoder defaultDecoder = new Default();

            @Override
            public Exception decode(String methodKey, Response response) {
                String body = "";
                try {
                    body = Util.toString(response.body().asReader());
                    // Assume partner‐service returns {"timeStamp":...,"message":...,"details":...}
                    var node = mapper.readTree(body);
                    String message = node.get("message").asText();
                    // throw your custom exception so GlobalExceptionHandler catches it
                    return new ResourceNotBelongToException(message);
                } catch (Exception e) {
                    // fallback to default (e.g. for non‐JSON errors)
                    return defaultDecoder.decode(methodKey, response);
                }
            }
        };
    }
}
