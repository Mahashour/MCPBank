package com.example.banksys.client.interfaces;

import com.example.banksys.client.domain.exception.InvalidNationalIdException;
import com.example.banksys.client.domain.model.NationalId;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class NationalIdDeserializer extends StdDeserializer<NationalId> {
    public NationalIdDeserializer() {
        super(NationalId.class);
    }

    @Override
    public NationalId deserialize(JsonParser p, DeserializationContext ctxt){
        JsonNode node = p.readValueAsTree();
        String type = node.get("type").asString();
        String value = node.get("value").asString();

        return NationalId.makeNationalId(type, value);
    }
}
