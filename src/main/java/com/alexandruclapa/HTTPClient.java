package com.alexandruclapa;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class HTTPClient {
    private static HTTPClient instance = null;
    private HttpClient client;

    private HTTPClient() {
        // Defeats only to defeat instantiation.
        client = HttpClientBuilder.create().build();
    }

    public static HTTPClient getInstance() {
        if (instance == null) {
            instance = new HTTPClient();
        }
        return instance;
    }

    public JSONObject GET(String urlString, HashMap<String, Object> parameters) throws IOException {
        String url = urlString;
        if (parameters != null) {
            url += "?";
            for (String key : parameters.keySet()) {
                url += key + "=" + parameters.get(key) + "&";
            }
        }

        System.out.println(url);
        HttpGet request = new HttpGet(url);

        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject responseObject = new JSONObject(result.toString());

        return responseObject;
    }

    public JSONObject POST(String urlString, JSONObject jsonObject) throws IOException {
        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost(urlString);
        postMethod.setEntity(requestEntity);

        HttpResponse response = client.execute(postMethod);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject responseObject = new JSONObject(result.toString());

        return responseObject;
    }

    public JSONObject PATCH(String urlString, JSONObject jsonObject) throws IOException {
        StringEntity requestEntity = new StringEntity(
                jsonObject.toString(),
                ContentType.APPLICATION_JSON);

        HttpPatch patchMethod = new HttpPatch(urlString);
        patchMethod.setEntity(requestEntity);

        HttpResponse response = client.execute(patchMethod);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        JSONObject responseObject = new JSONObject(result.toString());

        return responseObject;
    }
}
