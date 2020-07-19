package com.cotrack.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtils {

    public static <T> T mapJsonObject(String jsonString, Class<T> classObject){
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Unwrapping: " + jsonString);
        //objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T mappedObject = null;
        try {
            mappedObject = objectMapper.readValue(jsonString, classObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mappedObject;
    }
}
