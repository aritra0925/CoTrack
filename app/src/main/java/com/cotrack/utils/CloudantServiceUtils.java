package com.cotrack.utils;

import android.util.Log;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.cotrack.BuildConfig;
import com.cotrack.models.ServiceDetails;
import java.net.MalformedURLException;
import java.net.URL;
import static com.cloudant.client.api.query.Expression.eq;
import static com.cloudant.client.api.query.Operation.not;

public class CloudantServiceUtils {

    private static final String LOG = "Service DB Activity";
    public static final String DB = "service";

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

    public static QueryResult<ServiceDetails> queryData(Selector selector) {
        Database database = cloudantConnect();
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<ServiceDetails> queryResult = database.query(queryBuilder.build(), ServiceDetails.class);
        Log.d(LOG, "Query result retreived: '" + queryResult + "' from " + DB + "'");
        return queryResult;
    }

    public static QueryResult<ServiceDetails> getAllData() {
        Database database = cloudantConnect();
        Selector selector = not(eq("asset_id", ""));
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<ServiceDetails> queryResult = database.query(queryBuilder.build(), ServiceDetails.class);
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

    public static QueryResult<ServiceDetails> cloudantGetWithPrimaryKey(String key, String value) {
        Database database = cloudantConnect();
        Selector selector = eq(key, value);
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<ServiceDetails> queryResult = database.query(queryBuilder.build(), ServiceDetails.class);
        Log.d(LOG, "Query result retreived: '" + queryResult + "' from " + DB + "'");
        return queryResult;
    }

    public static boolean insertDocument(ServiceDetails document) {
        Database database = cloudantConnect();
        database.save(document);
        Log.d(LOG, "You have inserted the document");
        return true;
    }

    public static boolean updateDocument(ServiceDetails document) {
        Database database = cloudantConnect();
        database.update(document);
        Log.d(LOG, "You have inserted the document");
        return true;
    }

    public static boolean checkEntry(String _id) {
        boolean flag = true;
        Database database = cloudantConnect();
        try {
            ServiceDetails object = database.find(ServiceDetails.class, _id);
        } catch (NoDocumentException ex) {
            return false;
        }
        Log.d(LOG, "Validating entry status: '" + flag + "' from " + DB + "'");
        return flag;
    }

    public static ServiceDetails getWithId(String _id) {
        boolean flag = true;
        Database database = cloudantConnect();
        ServiceDetails object = database.find(ServiceDetails.class, _id);
        return object;
    }

    public static boolean checkEntry(String key, String value) {
        Database database = cloudantConnect();
        Selector selector = eq(key, value);
        QueryBuilder queryBuilder = new QueryBuilder(selector);
        QueryResult<ServiceDetails> queryResult = database.query(queryBuilder.build(), ServiceDetails.class);
        boolean flag = queryResult.getDocs().size() > 0;
        Log.d(LOG, "Validating entry status: '" + flag + "' from " + DB + "'");
        return flag;
    }

}