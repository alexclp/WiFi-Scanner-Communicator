import java.util.ArrayList;

public class Main {
    public static void main(String[] arg) {
        DataUploader.getInstance().createTempDataOnServer();

        MeasurementsParser measurementsParser = new MeasurementsParser();
        ArrayList<Measurement> measurementArrayList = measurementsParser.parseFile("src/main/resources/output.txt");
        System.out.println(measurementArrayList);

        for (Measurement measurement : measurementArrayList) {
            try {
                DataUploader.getInstance().uploadMeasurementFor(1, measurement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
