package com.cotrack.utils;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.BasicAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;

public class WatsonUtils {
    public static final String SERVICE = "2018-03-16";
    public static final String VERSION_DATE = "service";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    public String analyzeWatson(){

        //Creating the class of the service. Make sure to insert you service username and password.
        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                VERSION_DATE,
                SERVICE,
                new BasicAuthenticator(USERNAME,PASSWORD)
        );

        //The text we want to analyze. You can insert any other text you like.
        String text = "IBM is an American multinational technology " +
                "company headquartered in Armonk, New York, " +
                "United States, with operations in over 170 countries.";

        //Entities and keywords are parameters you get back from the service about your text.
        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        Features features = new Features.Builder()
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        //Take the parameters and send them to your service for resutls.
        Response<AnalysisResults> response = service
                .analyze(parameters)
                .execute();

        //print the result
        System.out.println(response);
        return response.toString();
    }

}
