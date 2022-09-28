module home.doorbell2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.eclipse.paho.client.mqttv3;


    opens home.doorbell2 to javafx.fxml;
    exports home.doorbell2;
}