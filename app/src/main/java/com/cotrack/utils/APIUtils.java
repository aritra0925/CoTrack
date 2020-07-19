package com.cotrack.utils;

import static com.cloudant.client.api.query.Expression.eq;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.google.gson.internal.LinkedTreeMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class APIUtils {

    public static final String URL = "https://ff37d895-652c-42c6-9987-c0f9fbbf5bc4-bluemix.cloudantnosqldb.appdomain.cloud";
    public static final String API_KEY = "ngpeWlOu1fUpz4y9zrFLCTnvF4Aq3WwZ2_pDjHNAA_yt";
    public static final String DB = "provider";
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
        Selector selector = eq(key, value);
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        System.out.println(queryResult.getDocs().get(0).toString());
        return queryResult;
    }

    public static void insertDocument(String document){
        Database database = cloudantConnect();
        //database.save(new ExampleDocument(true));

        // For Service
        //database.save(new ServiceDetails("service:Hospital", "service", "S001", "Hospital", "ASST001", null));

        // For User
        //database.save(new UserDetails("user:U0001","user","Piu", "Piu_009", "Piu@2020", "Kamardanga", "Howrah", "WB", "711104", "9080706050", null, "Hospital", null));

        // For Provider
        //database.save(new ProviderDetails("provider:P0002", "provider", "Ruby Hospital", "R_123", "R@2020", "Saltlake", "Kolkata", "WB", "700099", "8765432110", "ruby12@gmail.com", "Hospital", "ASST002", null));
        System.out.println("You have inserted the document");

        // Get an ExampleDocument out of the database and deserialize the JSON into a Java type
        //ExampleDocument doc = database.find(ExampleDocument.class,"example_id");

        // For service
        //ServiceDetails doc = database.find(ServiceDetails.class, "service");

        // For User
        //UserDetails doc = database.find(UserDetails.class, "user");

        //For Provider
        //ProviderDetails doc = database.find(ProviderDetails.class, "provider:P0002");

        //System.out.println(doc);
        QueryResult<Object> queryResult = cloudantGetWithPrimaryKey("provider_name", "Ruby Hospital");
        System.out.println(queryResult.getDocs().get(0).toString());

        LinkedTreeMap<String, String> providerDetails = (LinkedTreeMap) queryResult.getDocs().get(0);
        //JsonProviderDetails jsonProviderDetails = JSONUtils.mapJsonObject(queryResult.getDocs().get(0).toString(), JsonProviderDetails.class);
        System.out.println("Response: " + providerDetails.get("provider_address1"));
    }

}
