sudo rm src/main/resources/output.txt
sudo iwlist wlan0 scan | egrep "Cell|ESSID|Signal" >> src/main/resources/output.txt

mvn install
mvn compile
mvn exec:java

