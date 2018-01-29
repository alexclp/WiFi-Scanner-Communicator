# sudo rm src/main/resources/output.txt
# sudo iwlist wlan0 scan | egrep "Cell|ESSID|Signal" >> src/main/resources/output.txt

# mvn install
# mvn compile
# mvn exec:java
sudo rm /home/pi/Work/Individual\ Project/WiFi-Scanner-Communicator/src/main/resources/output.txt
sudo iwlist wlan0 scan | egrep "Cell|ESSID|Signal" >> /home/pi/Work/Individual\ Project/WiFi-Scanner-Communicator/src/main/resources/output.txt
mvn -f /home/pi/Work/Individual\ Project/WiFi-Scanner-Communicator/pom.xml exec:java
