package com.alexandruclapa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MeasurementsParser {
    public ArrayList<Measurement> parseFile(String fileName) {
        ArrayList<Measurement> measurements = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                Measurement measurement = new Measurement(null, 0, null);
                for (int i = 0; i < 3; ++i) {
                    if (line.contains("Address")) {
                        int startIndex = line.indexOf(":") + 2;
                        int endIndex = line.lastIndexOf(":") + 3;
                        measurement.setMacAddress(line.substring(startIndex, endIndex));
                    } else if (line.contains("Signal level")) {
                        int startIndex = line.lastIndexOf("=") + 1;
                        int endIndex = startIndex + 3;
                        measurement.setSignalStrength(Integer.parseInt(line.substring(startIndex, endIndex)));
                    } else if (line.contains("ESSID:")) {
                        int startIndex = line.indexOf(":") + 2;
                        int endIndex = line.lastIndexOf("\"");
                        measurement.setSsid(line.substring(startIndex, endIndex));
                    }
                    line = reader.readLine();
                }

                measurements.add(measurement);
            }

            reader.close();
            return measurements;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", fileName);
            e.printStackTrace();
            return null;
        }
    }
}