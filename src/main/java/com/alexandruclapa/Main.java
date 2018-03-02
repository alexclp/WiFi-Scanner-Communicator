package com.alexandruclapa;

import org.json.JSONObject;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) {
        // First check if scan flag is true

        boolean shouldScan = false;
        boolean storeData = false; // if this one is true then the data must be passed to another route in order to determine the current location etc.
        int locationID = -1;
        try {
            JSONObject response = HTTPClient.getInstance().GET("https://scanner-on-off.herokuapp.com/scanSwitch/1", null);
            if (response.has("shouldScan")) {
                shouldScan = response.getBoolean("shouldScan");
            }

            if (response.has("locationID")) {
                locationID = response.getInt("locationID");
            }

            if (response.has("storeData")) {
                storeData = response.getBoolean("storeData");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // If it's true then commence scanning

        if (shouldScan && locationID != -1) {
            MeasurementsParser measurementsParser = new MeasurementsParser();
            ArrayList<Measurement> measurementArrayList = measurementsParser.parseFile("/home/pi/Work/Individual Project/WiFi-Scanner-Communicator/src/main/resources/output.txt");
            System.out.println(measurementArrayList);

            if (storeData) {
                for (Measurement measurement : measurementArrayList) {
                    try {
                        DataUploader.getInstance().uploadMeasurementFor(locationID, measurement);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (Measurement measurement : measurementArrayList) {
                    try {
                        DataUploader.getInstance().uploadTempData(measurement);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // And set it to false at the end

            try {
                JSONObject requestJSON = new JSONObject();
                requestJSON.put("shouldScan", false);
                HTTPClient.getInstance().PATCH("https://scanner-on-off.herokuapp.com/scanSwitch/1", requestJSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
