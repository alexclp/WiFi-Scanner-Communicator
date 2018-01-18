public class Measurement {
    private String ssid;
    private int signalStrength;
    private String macAddress;

    public Measurement(String ssid, int signalStrength, String macAddress) {
        this.ssid = ssid;
        this.signalStrength = signalStrength;
        this.macAddress = macAddress;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "ssid='" + ssid + '\'' +
                ", signalStrength=" + signalStrength +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }
}