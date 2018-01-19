import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class DataUploader {
    private static DataUploader instance = null;

    private DataUploader() {
        // Exists only to defeat instantiation.
    }

    public static DataUploader getInstance() {
        if (instance == null) {
            instance = new DataUploader();
        }
        return instance;
    }

    public boolean uploadMeasurementFor(int locationID, Measurement measurement) throws IOException {
        String apID = getAccessPointIDFor(measurement);

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("locationID", locationID);
        requestJSON.put("apID", apID);
        requestJSON.put("signalStrength", measurement.getSignalStrength());

        JSONObject responseJSON = HTTPClient.getInstance().POST("http://localhost:8080/measurements", requestJSON);
        if (responseJSON.get("id") != null) {
            return true;
        }

        return false;
    }

    private String getAccessPointIDFor(Measurement measurement) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("macAddress", measurement.getMacAddress());
        JSONObject jsonObject = HTTPClient.getInstance().GET("http://localhost:8080/address/" + measurement.getMacAddress(), null);
        String apID;
        if (jsonObject.get("macAddress") != null) {
            apID = String.format("%d", jsonObject.getInt("id"));
        } else {
            JSONObject requestJSON = new JSONObject();
            jsonObject.put("name", measurement.getSsid());
            jsonObject.put("macAddress", measurement.getMacAddress());
            jsonObject = HTTPClient.getInstance().POST("http://localhost:8080/accessPoints", requestJSON);
            apID = String.format("%d", jsonObject.getInt("id"));
        }

        return apID;
    }
}
