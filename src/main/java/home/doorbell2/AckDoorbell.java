package home.doorbell2;

import java.util.Timer;
import java.util.TimerTask;

public class AckDoorbell implements Runnable{
    Doorbell2Controller stage;
    String topic;
    String message;

    AckDoorbell(Doorbell2Controller doorbellController){
        this.stage = doorbellController;
    }

    @Override
    public void run(){
        this.topic = stage.topic;
        this.message = stage.message;

        switch(topic){
            case "zigbee2mqtt/New_Doorbell_Button/action": {
                //System.out.println("Received Message");
                stage.ring.seek(stage.greeting.getStartTime());
                stage.ring.play();
                try {
                    stage.switchImage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    stage.greeting.seek(stage.greeting.getStartTime());
                    stage.greeting.play();
                }
                },1000);

                break;
            }
            default: {
                break;
            }
        }

    }
}
