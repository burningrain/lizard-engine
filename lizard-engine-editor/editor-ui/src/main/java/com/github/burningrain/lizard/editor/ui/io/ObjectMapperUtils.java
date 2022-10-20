package com.github.burningrain.lizard.editor.ui.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapperUtils {

    private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();
    private static final MapType TYPE = TYPE_FACTORY.constructMapType(HashMap.class, String.class, String.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Serializable createDataFromString(ElementDataConverter elementDataConverter, String d) {
        return elementDataConverter.importNodeData(convertToMap(d));
    }

    public static String createDataFormModel(ElementDataConverter elementDataConverter, Serializable d) {
        return convertToString((elementDataConverter.exportNodeData(d)));
    }

    private static String convertToString(Map<String, String> map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> convertToMap(String s) {
        try {
            return OBJECT_MAPPER.readValue(s, TYPE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
