package com.bluecoat.proxy.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Component
public class GenericDecoder implements Decoder {

    private final ObjectMapper objectMapper;

    public GenericDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException {
        String responseBody = new String(response.body().asInputStream().readAllBytes());

        // Handle ResponseEntity<R>
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();

            if (rawType == ResponseEntity.class) {
                Type responseType = parameterizedType.getActualTypeArguments()[0];
                Object decodedResponseBody;

                if (isJson(responseBody)) {
                    decodedResponseBody = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructType(responseType));
                } else {
                    decodedResponseBody = responseBody;
                }
                return ResponseEntity.ok(decodedResponseBody);
            }
        }

        // Handle standard class types like String.class
        if (type instanceof Class) {
            return handleClassType(responseBody, (Class<?>) type);
        }

        throw new DecodeException(response.status(), "Unsupported response type", response.request());
    }

    private Object handleClassType(String responseBody, Class<?> type) throws IOException {
        return objectMapper.readValue(responseBody, type);
    }

    private boolean isJson(String responseBody) {
        try {
            objectMapper.readTree(responseBody);
            return true;
        } catch (IOException e) {
            return false;  // If parsing fails, it's not JSON
        }
    }
}
