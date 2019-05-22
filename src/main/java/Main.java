import agent.Agent;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        System.out.println("\nWelcome to SOFT-ALARM-BOT! I will be your personal agent.\n");
        System.out.println("First of all, some information about you:");
        System.out.println("If you have no events planned, at what time should I wake you up? ");
        String maxAlarm = sc.nextLine();
        System.out.println("How long do you take, on average, to get ready and leave in the morning? ");
        String defaultReady = sc.nextLine();

        try {
            int day;
            Agent agent = new Agent();
            agent.setMaxWakeUp(maxAlarm);
            agent.setDefaultReady(defaultReady);

            for(day=20; day<31; day = (day % 30) +1) {
                String dayStr = day < 10 ? "0" + day : "" + day;
                String date = "2019-05-" + dayStr + "T00:00:00Z";
                System.out.println("\n[INFO] Setting alarm for 2019-05-" + dayStr);
                String alarm = agent.processData(date);
                System.out.println("\n[ALARM] 2019-05-" + dayStr + " : " + alarm);
                int reward = 0;
                while (true) {
                    System.out.println("[FEEDBACK] How much did you appreciate the alarm setting? 1 (very bad) - 5 (very good)");
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
                //agent.printQTable();
            }


            agent.storePastEvents();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
