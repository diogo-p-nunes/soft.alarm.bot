import agent.Agent;
import agent.Maps;
import com.google.maps.model.DistanceMatrix;
import user.UserInfo;

public class Main {

    public static void main(String[] args) {

        // try and build the user module
        //UserInfo user;
        try {
            //user = new UserInfo();
            //Maps maps = new Maps();
            Agent agent = new Agent();
            System.out.println("Alarm set for: " + agent.setAlarm());
            /*
            System.out.println(user.getFirstEventStart("2019-05-02T00:00:00Z"));
    		System.out.println(user.getFirstEventLocation("2019-05-02T00:00:00Z"));

            for(int i = 0; i < 2; i++) {
                System.out.println();
                String date = "2019-05-02T00:00:00Z";
                String origem = user.getUserLocation();
                String destino = user.getFirstEventLocation(date);
                String meioTransporte = user.getFirstEventTransportation(date);

                DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);
                maps.printDistanceMatrix(matrix);
                
            }
            */
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
