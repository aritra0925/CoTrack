package com.cotrack.utils;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.BasicAuthenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;

import java.util.List;
import java.util.logging.LogManager;

public class WatsonUtils {
    public static final String SERVICE = "service";
    public static final String VERSION_DATE = "2018-03-16";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    public static String analyzeWatson(){

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

    public static MessageResponse startService(String assistant_apikey, String assistant_url, String workspace_id, String inputText) {
        // Suppress log messages in stdout.
        LogManager.getLogManager().reset();

        // Set up Assistant service.
        Authenticator authenticator = new IamAuthenticator(assistant_apikey); // replace with API key
        Assistant service = new Assistant("2019-02-28", authenticator);
        //service.setServiceUrl(assistant_url);
        String assistantId = workspace_id; // replace with assistant ID

        // Create session.
        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistantId).build();
        SessionResponse session = service.createSession(createSessionOptions).execute().getResult();
        String sessionId = session.getSessionId();
        // Initialize with empty message to start the conversation.
        MessageInput input = new MessageInput.Builder()
                .messageType("text")
                .text(inputText)
                .build();
        // Start conversation with empty message.
        MessageOptions messageOptions = new MessageOptions.Builder(assistantId,
                sessionId).input(input).build();
        MessageResponse response = service.message(messageOptions).execute().getResult();

        // Print the output from dialog, if any. Assumes a single text response.
        List<RuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
        if (responseGeneric.size() > 0) {
            if (responseGeneric.get(0).responseType().equals("text")) {
                System.out.println(responseGeneric.get(0).text());
            }
        }
        // We're done, so we delete the session.
        DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
        service.deleteSession(deleteSessionOptions).execute();
        return response;

    }
}
