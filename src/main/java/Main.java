import agent.Agent;
import agent.Maps;
import com.google.maps.model.DistanceMatrix;
import user.UserInfo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            int day;
            Agent agent = new Agent();

            for(day=6; day<30; day++) {
                String dayStr = day < 10 ? "0" + day : "" + day;
                String date = "2019-05-" + dayStr + "T00:00:00Z";
                String alarm = agent.processData(date);
                System.out.println("\n[ALARM] 2019-05-" + dayStr + " : " + alarm + "\n");
                int reward = 0;
                while (true) {
                    System.out.println("\n[FEEDBACK] Did you arrive on time? 1 (very bad) - 5 (very good)");
                    reward = sc.nextInt();
                    if (!(reward < 1 || reward > 5)) {
                        break;
                    }
                }
                if (reward == 1) {
                    reward = -2;
                }
                if (reward == 2) {
                    reward = -1;
                }
                if (reward == 3) {
                    reward = 0;
                }
                if (reward == 4) {
                    reward = 1;
                }
                if (reward == 5) {
                    reward = 2;
                }
                agent.processReward(reward);
            }

            agent.storePastEvents();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
