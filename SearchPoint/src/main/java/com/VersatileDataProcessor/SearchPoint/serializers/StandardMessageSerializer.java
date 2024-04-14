package com.versatileDataProcessor.searchPoint.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

@Slf4j
public class StandardMessageSerializer extends JsonSerializer<StandardMessage> {

    @Override
    public void serialize(StandardMessage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject(); // Start writing the JSON object

        // Get all fields of the StandardMessage class
        Field[] fields = StandardMessage.class.getDeclaredFields();

        // Iterate over each field
        for (Field field : fields) {
            field.setAccessible(true); // Ensure we can access private fields
            try {
                Object fieldValue = field.get(value);
                // Only write the field if its value is not null
                if (fieldValue != null) {
                    gen.writeObjectField(field.getName(), fieldValue);
                }
            } catch (IllegalAccessException exception) {
                // Handle IllegalAccessException if necessary
                log.error("Error Serializing StandardMessage : {} : {}", exception.getClass().getName(), exception.getMessage());
            }
        }
        gen.writeEndObject(); // End writing the JSON object
    }
}
