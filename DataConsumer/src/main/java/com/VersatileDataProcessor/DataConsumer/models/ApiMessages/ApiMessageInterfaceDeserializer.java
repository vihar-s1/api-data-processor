package com.VersatileDataProcessor.DataConsumer.models.ApiMessages;


import com.VersatileDataProcessor.DataConsumer.models.DataSource;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ApiMessageInterfaceDeserializer extends JsonDeserializer<ApiMessageInterface> {
    @Override
    public ApiMessageInterface deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);

        // currently only one type is there
        String dataSource = root.get("dataSource").asText();

        switch (DataSource.valueOf(dataSource)){
            case DataSource.MOCK:
                return mapper.readValue(root.toString(), MockApiMessage.class);
            default:
                throw new IllegalArgumentException("Unknown data source: " + dataSource);
        }
    }
}
