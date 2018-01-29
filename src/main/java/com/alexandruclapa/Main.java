package com.alexandruclapa;

import org.json.JSONObject;

import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) {
        // First check if scan flag is true

        boolean shouldScan = false;
        try {
            JSONObject response = HTTPClient.getInstance().GET("https://scanner-on-off.herokuapp.com/scanSwitch/1", null);
            if (response.has("shouldScan")) {
                shouldScan = response.getBoolean("shouldScan");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // If it's true then commence scanning

        if (shouldScan) {
            DataUploader.getInstance().createTempDataOnServer();

            MeasurementsParser measurementsParser = new MeasurementsParser();
            ArrayList<Measurement> measurementArrayList = measurementsParser.parseFile("/home/pi/Work/Individual Project/WiFi-Scanner-Communicator/src/main/resources/output.txt");
            System.out.println(measurementArrayList);

            for (Measurement measurement : measurementArrayList) {
                try {
                    DataUploader.getInstance().uploadMeasurementFor(1, measurement);
                } catch (Exception e) {
                    e.printStackTrace();
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
