import agent.Agent;
import agent.Maps;
import com.google.maps.model.DistanceMatrix;
import user.UserInfo;

public class Main {

    public static void main(String[] args) {

        try {
            Agent agent = new Agent();
            String date = "2019-05-02T00:00:00Z";
            agent.setAlarm(date);
            agent.storePastEvents();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
