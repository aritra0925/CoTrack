package com.cotrack.models;

import java.util.Random;

// A Java type that can be serialized to JSON
public class ExampleDocument {
    private String _id = "example_id." + Integer.toString(new Random().nextInt(900));
    private String _rev = null;
    private boolean isExample;

    public ExampleDocument(boolean isExample) {
        this.isExample = isExample;
    }

    public String toString() {
        return "{ id: " + _id + ",\nisExample: " + isExample + "\n}";
    }
}