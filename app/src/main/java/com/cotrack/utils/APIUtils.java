package com.cotrack.utils;

import android.content.Context;
import android.util.Log;
import static com.cloudant.client.api.query.EmptyExpression.empty;
import static com.cloudant.client.api.query.Expression.eq;
import static com.cloudant.client.api.query.Expression.gt;
import static com.cloudant.client.api.query.Operation.and;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.cotrack.models.ExampleDocument;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class APIUtils {

    public static final String URL = "https://ff37d895-652c-42c6-9987-c0f9fbbf5bc4-bluemix.cloudantnosqldb.appdomain.cloud";
    public static final String API_KEY = "ngpeWlOu1fUpz4y9zrFLCTnvF4Aq3WwZ2_pDjHNAA_yt";
    public static final String DB = "cotrack";
    static String testDoc = "{id:isExample:true}";

    public static Database cloudantConnect() {
        // Create a new CloudantClient instance for account endpoint example.cloudant.com
        CloudantClient client = null;
        try {
            client = ClientBuilder.url(new URL(URL)).iamApiKey(API_KEY).build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String dbReturn = "";
        // Get a List of all the databases this Cloudant account
        List<String> databases = ((CloudantClient) client).getAllDbs();
        Database db = client.database(DB, false);
        System.out.println("Conneected to server version: ' " + client.serverVersion() + "' \nDatabase: '" + DB +"'");
        return db;
    }

    public static QueryResult<Object> cloudantGetWithPrimaryKey(String key, String value) {
        Database database = cloudantConnect();
        Selector selector = eq("Test", "test text");
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        System.out.println(queryResult.getDocs().get(0).toString());
        return queryResult;
    }

    public static void insertDocument(String document){
        Database database = cloudantConnect();
        database.save(new ExampleDocument(true));
        System.out.println("You have inserted the document");

        // Get an ExampleDocument out of the database and deserialize the JSON into a Java type
        ExampleDocument doc = database.find(ExampleDocument.class,"example_id");
        System.out.println(doc);
    }

}
