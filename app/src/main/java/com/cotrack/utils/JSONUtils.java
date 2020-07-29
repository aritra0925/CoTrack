package com.cotrack.utils;

import com.cotrack.models.Message;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.apache.commons.codec.Charsets;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static <Type> void saveObjectToJson(Object object, FileOutputStream fileOutputStream) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            //FileOutputStream fileOutputStream = new FileOutputStream(filenamePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charsets.UTF_8.name());
            Writer writer = new BufferedWriter(outputStreamWriter);
            mapper.writeValue(writer, object);
            fileOutputStream.close();
            outputStreamWriter.close();
            writer.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static List<Message> mapJsonArrayObject(String jsonString, Class<Message> classObject){
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructType(classObject);
        System.out.println("Unwrapping: " + jsonString);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
       // objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //List<T> mappedObject = null;
        Message[] messageArrayList=null;
        try {
            messageArrayList = objectMapper.readValue(jsonString, Message[].class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList(messageArrayList);
    }

    public static <Message> List<Message> readObjectListFromFile(String jsonString, Class<com.cotrack.models.Message> returnedObjectClass) {
        List<Message> objectList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        final CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, returnedObjectClass);
        try {
            objectList = mapper.readValue(jsonString, collectionType);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return objectList;
    }

}
