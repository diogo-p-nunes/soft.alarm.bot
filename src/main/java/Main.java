import agent.Maps;
import com.google.maps.model.DistanceMatrix;
import user.UserInfo;

public class Main {

    public static void main(String[] args) {

        // try and build the user module
        UserInfo user;
        try {
            user = new UserInfo();
            Maps maps = new Maps();

            for(int i = 0; i < 2; i++) {
                System.out.println();
                String date = "2019-05-02T00:00:00Z";
                String origem = user.getUserLocation();
                String destino = user.getFirstEventLocation(date);
                String meioTransporte = user.getFirstEventTransportation(date);

                DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);
                maps.printDistanceMatrix(matrix);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }


}
