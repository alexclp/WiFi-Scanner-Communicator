import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) {
        MeasurementsParser measurementsParser = new MeasurementsParser();
        ArrayList<Measurement> measurementArrayList = measurementsParser.parseFile("src/main/resources/output.txt");

        for (Measurement measurement : measurementArrayList) {
            try {
                DataUploader.getInstance().uploadMeasurementFor(1, measurement);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
