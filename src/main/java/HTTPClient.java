import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void GET(String urlString) throws IOException {
        String url = "http://www.google.com/search?q=httpClient";

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
    }

    public void POST(String urlString, Measurement measurement) throws IOException {
        String jsonString = new JSONObject()
                .put("ssid", measurement.getSsid())
                .put("macAddress", measurement.getMacAddress())
                .put("signalStrength", String.format("%d", measurement.getSignalStrength()))
                .toString();

        StringEntity requestEntity = new StringEntity(
                jsonString,
                ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost(urlString + "extension");
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
    }

    public static void main(String[] args) {
        try {
            HTTPClient.getInstance().GET("aa");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
