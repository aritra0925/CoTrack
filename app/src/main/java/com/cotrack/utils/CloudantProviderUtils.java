package com.cotrack.utils;

import android.util.Log;

import static com.cloudant.client.api.query.Expression.eq;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.cotrack.BuildConfig;
import com.google.gson.internal.LinkedTreeMap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CloudantProviderUtils {

    private static final String LOG = "Provider DB Activity";
    public static final String DB = "provider";

    public static Database cloudantConnect() {
        // Create a new CloudantClient instance for account endpoint example.cloudant.com
        CloudantClient client = null;
        try {
            client = ClientBuilder.url(new URL(BuildConfig.CLOUDANT_URL)).iamApiKey(BuildConfig.CLOUDANT_API_KEY).build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Database db = client.database(DB, false);
        Log.d(LOG, "Conneected to server version: ' " + client.serverVersion() + "' \nDatabase: '" + DB + "'");
        return db;
    }

    public static QueryResult<Object> queryData(Selector selector) {
        Database database = cloudantConnect();
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        Log.d(LOG, "Query result retreived: '" + queryResult + "' from " + DB + "'");
        return queryResult;
    }

    public static boolean validateEntry(Selector selector) {
        Database database = cloudantConnect();
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        boolean flag = queryResult.getDocs().size() > 0;
        Log.d(LOG, "Validating entry status: '" + flag + "' from " + DB + "'");
        return flag;
    }

    public static QueryResult<Object> cloudantGetWithPrimaryKey(String key, String value) {
        Database database = cloudantConnect();
        Selector selector = eq(key, value);
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        Log.d(LOG, "Query result retreived: '" + queryResult + "' from " + DB + "'");
        return queryResult;
    }

    public static <T> boolean insertDocument(T document) {
        Database database = cloudantConnect();
        database.save(document);
        Log.d(LOG, "You have inserted the document");
        return true;
    }

    public static <T> boolean checkEntry(Class<T> classType, String _id) {
        boolean flag = true;
        Database database = cloudantConnect();
        try {
            T object = database.find(classType, _id);
        } catch (NoDocumentException ex) {
            return false;
        }
        Log.d(LOG, "Validating entry status: '" + flag + "' from " + DB + "'");
        return flag;
    }

    public static <T> boolean checkEntry(String key, String value) {
        Database database = cloudantConnect();
        Selector selector = eq(key, value);
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<Object> queryResult = database.query(queryBuilder.build(), Object.class);
        boolean flag = queryResult.getDocs().size() > 0;
        Log.d(LOG, "Validating entry status: '" + flag + "' from " + DB + "'");
        return flag;
    }

}


//For Provider
//ProviderDetails doc = database.find(ProviderDetails.class, "provider:P0002");

//System.out.println(doc);
        /*QueryResult<Object> queryResult = cloudantGetWithPrimaryKey("provider_name", "Ruby Hospital");
        System.out.println(queryResult.getDocs().get(0).toString());*/

        /*LinkedTreeMap<String, String> providerDetails = (LinkedTreeMap) queryResult.getDocs().get(0);
        //JsonProviderDetails jsonProviderDetails = JSONUtils.mapJsonObject(queryResult.getDocs().get(0).toString(), JsonProviderDetails.class);
        System.out.println("Response: " + providerDetails.get("provider_address1"));*/

// Get an ExampleDocument out of the database and deserialize the JSON into a Java type
//ExampleDocument doc = database.find(ExampleDocument.class,"example_id");

// For service
//ServiceDetails doc = database.find(ServiceDetails.class, "service");

// For User
//UserDetails doc = database.find(UserDetails.class, "user");

// For Service
//database.save(new ServiceDetails("service:Hospital", "service", "S001", "Hospital", "ASST001", null));

// For User
//database.save(new UserDetails("user:U0001","user","Piu", "Piu_009", "Piu@2020", "Kamardanga", "Howrah", "WB", "711104", "9080706050", null, "Hospital", null));

// For Provider
//database.save(new ProviderDetails("provider:P0002", "provider", "Ruby Hospital", "R_123", "R@2020", "Saltlake", "Kolkata", "WB", "700099", "8765432110", "ruby12@gmail.com", "Hospital", "ASST002", null));
