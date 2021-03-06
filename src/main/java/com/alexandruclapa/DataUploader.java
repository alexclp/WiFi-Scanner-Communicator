package com.alexandruclapa;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class DataUploader {
    private static DataUploader instance = null;
    final String baseURL = "https://wifi-nav-api.herokuapp.com";

    private DataUploader() {
        // Exists only to defeat instantiation.
    }

    public static DataUploader getInstance() {
        if (instance == null) {
            instance = new DataUploader();
        }
        return instance;
    }

    public void createTempDataOnServer() {
        try {
            int roomID = createRoom("Test-Room");
            if (roomID != 0) {
                if (!createLocation(0.0, 0.0, 0.0, String.format("%d", roomID))) {
                    throw new Exception("Creating location failed");
                }
            } else {
                throw new Exception("Creating a room failed.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private int createRoom(String name) throws IOException {
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("name", name);
        JSONObject responseJSON = HTTPClient.getInstance().POST(baseURL + "/rooms", requestJSON);
        if (responseJSON.has("id")) {
            return responseJSON.getInt("id");
        }
        return 0;
    }

    private boolean createLocation(Double latitude, Double longitude, Double pressure, String roomID) throws IOException {
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("latitude", latitude);
        requestJSON.put("longitude", longitude);
        requestJSON.put("pressure", pressure);
        requestJSON.put("roomID", roomID);
        JSONObject responseJSON = HTTPClient.getInstance().POST(baseURL + "/locations", requestJSON);
        return responseJSON.has("id");
    }

    public boolean uploadMeasurementFor(int locationID, Measurement measurement) throws IOException {
        String apID = getAccessPointIDFor(measurement);
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("locationID", locationID);
        requestJSON.put("apID", apID);
        requestJSON.put("signalStrength", measurement.getSignalStrength());

        JSONObject responseJSON = HTTPClient.getInstance().POST(baseURL + "/measurements", requestJSON);
        if (responseJSON.get("id") != null) {
            return true;
        }

        return false;
    }

    public boolean uploadTempData(Measurement measurement) throws IOException {
        String url = "https://scanner-on-off.herokuapp.com";
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("signalStrength", measurement.getSignalStrength());
        requestJSON.put("name", measurement.getSsid());
        requestJSON.put("macAddress", measurement.getMacAddress());

        JSONObject responseJSON = HTTPClient.getInstance().POST(url + "/measurements", requestJSON);
        if (responseJSON.get("id") != null) {
            return true;
        }

        return false;
    }

    private String getAccessPointIDFor(Measurement measurement) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("macAddress", measurement.getMacAddress());
        JSONObject jsonObject = HTTPClient.getInstance().GET(baseURL + "/accessPoints/address/" + measurement.getMacAddress(), null);
        String apID;
        System.out.println(jsonObject);
        if (jsonObject.has("macAddress")) {
            apID = String.format("%d", jsonObject.getInt("id"));
        } else {
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("name", measurement.getSsid());
            requestJSON.put("macAddress", measurement.getMacAddress());
            jsonObject = HTTPClient.getInstance().POST(baseURL + "/accessPoints", requestJSON);
            System.out.println("Request JSON " + requestJSON);
            System.out.println("Response JSON " + jsonObject);
            apID = String.format("%d", jsonObject.getInt("id"));
        }

        return apID;
    }
}
