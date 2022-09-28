package home.doorbell2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.eclipse.paho.client.mqttv3.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Doorbell2Controller implements MqttCallback, Initializable {

    @FXML
    private ImageView myImageView;

    MqttClient client;
    MqttConnectOptions connOpts;
    String topic;
    String message;
    AckDoorbell ackDoorbell;
    Image welcome;
    Image pleaseWait;
    Timer timer;
    String bellClipString,greetClipString;
    MediaPlayer ring,greeting;
    String clientID;




    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String separator = System.getProperty("file.separator");

        switch(separator){
            case "\\":
                bellClipString = new File("C:/Users/retac/temp/doorbell_short.mp3").toURI().toString();
                greetClipString = new File("C:/Users/retac/temp/greeting.mp3").toURI().toString();
                break;
            case "/":
                bellClipString = new File("/home/pi/resources/doorbell_short.mp3").toURI().toString();
                greetClipString = new File("/home/pi/resources/greeting.mp3").toURI().toString();
                break;
        }
        ring = new MediaPlayer(new Media(bellClipString));
        greeting = new MediaPlayer(new Media(greetClipString));
        pleaseWait = new Image(getClass().getResourceAsStream("Wait.jpg"));
        welcome = new Image(getClass().getResourceAsStream("Welcome.jpg"));
        timer = new Timer();
        ackDoorbell = new AckDoorbell(this);

        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName("mqtt");
        connOpts.setPassword("Eallen1ha".toCharArray());
        connOpts.setAutomaticReconnect(true);
        clientID = MqttAsyncClient.generateClientId();
        try {
            client = new MqttClient("tcp://192.168.5.200:1883", clientID);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                connect();
            }
        },5000);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void switchImage() throws InterruptedException {
        myImageView.setImage(pleaseWait);
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                myImageView.setImage(welcome);
            }
        },20000);
    }

    public void connect(){
        try {
            client.setCallback(this);
            //System.out.println("going to connect");
            client.connect(connOpts);
            //System.out.println("return from connect");

            topic = "zigbee2mqtt/New_Doorbell_Button/action";
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        client.subscribe(topic, 0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            },1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //System.out.println("message arrived");
        this.message = message.toString();
        this.topic = topic;
        Platform.runLater(ackDoorbell);
    }

    @Override
    public void connectionLost(Throwable cause) {
                connect();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

}