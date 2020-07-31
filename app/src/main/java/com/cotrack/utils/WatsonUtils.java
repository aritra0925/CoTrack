package com.cotrack.utils;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import java.util.logging.LogManager;

public class WatsonUtils {
    public static final String VERSION_DATE = "2019-02-28";


    public static MessageResponse startService(String assistant_apikey, String assistant_url, String workspace_id, String inputText) {
        // Suppress log messages in stdout.
        LogManager.getLogManager().reset();

        // Set up Assistant service.
        Authenticator authenticator = new IamAuthenticator(assistant_apikey); // replace with API key
        Assistant service = new Assistant(VERSION_DATE, authenticator);
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

        // We're done, so we delete the session.
        DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
        service.deleteSession(deleteSessionOptions).execute();
        return response;

    }
}
