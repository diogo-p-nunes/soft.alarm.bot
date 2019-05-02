package user;

import com.google.api.client.util.DateTime;
import exception.NoEventException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserInfo {
    private UserCalendar calendar;
    private String location;
    private Feedback feedback;

    public UserInfo() throws IOException, GeneralSecurityException {
        this.calendar = new UserCalendar();
        this.location = "Some random location";
        this.feedback = new Feedback();
    }

    public String getFirstEventStart(String dateString) throws NoEventException, IOException {
        return this.calendar.getFirstEventStart(stringToDateTime(dateString));
    }

    public String getFirstEventLocation(String dateString) throws NoEventException, IOException {
        return this.calendar.getFirstEventLocation(stringToDateTime(dateString));
    }

    public String getUserLocation() {
        return this.location;
    }

    private DateTime stringToDateTime(String dateString) {
        return new DateTime(dateString);
    }
}
