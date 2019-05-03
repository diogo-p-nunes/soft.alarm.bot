import user.UserInfo;


public class Main {
    public static void main(String[] args) {
        // try and build the user module
        UserInfo user;
        try {
            user = new UserInfo();

            // get the start time and location of the first event of tomorrow
            System.out.println(user.getFirstEventStart("2019-05-02T00:00:00Z"));
            System.out.println(user.getFirstEventLocation("2019-05-02T00:00:00Z"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
