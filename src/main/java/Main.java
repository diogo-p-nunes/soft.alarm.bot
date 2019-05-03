import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import user.UserInfo;

public class Main {

    private static final String APIKEY = "AIzaSyDcOZV_h67RGwOFz3CN9hLIcdnYUgWc3EI";

    public static void main(String[] args) {

        // try and build the user module
        UserInfo user;
        try {
            user = new UserInfo();

            // get the start time and location of the first event of tomorrow
            System.out.println(user.getFirstEventStart("2019-05-02T00:00:00Z"));
            System.out.println(user.getFirstEventLocation("2019-05-02T00:00:00Z"));

            // example API - change to MAPS
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(APIKEY)
                    .build();
            GeocodingResult[] results =  GeocodingApi.geocode(context,
                    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(results[0].addressComponents));


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
