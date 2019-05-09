import agent.Agent;
import agent.Maps;
import com.google.maps.model.DistanceMatrix;
import user.UserInfo;

public class Main {

    public static void main(String[] args) {

        try {
            int day;
            Agent agent = new Agent();

            for(day=5; day<13; day++) {
                String dayStr = day < 10 ? "0"+ day : ""+day;
                String date = "2019-05-" + dayStr + "T00:00:00Z";
                String alarm = agent.setAlarm(date);
                System.out.println("\n[ALARM] 2019-05-" + dayStr + " : " + alarm + "\n");
            }

            agent.storePastEvents();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
