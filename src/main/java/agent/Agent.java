package agent;

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.maps.model.DistanceMatrix;
import exception.GetTimeToEventException;
import exception.NoEventException;
import user.UserInfo;

public class Agent {
	
	private UserInfo user;
	private Maps maps;

	public Agent() throws IOException, GeneralSecurityException, ClassNotFoundException {
		user = new UserInfo();
		maps = new Maps();
	}
	
	public void setAlarm(String date) throws NoEventException, IOException, GetTimeToEventException {

		// Set date's first event data
		String origem = user.getUserLocation();
		String destino = user.getFirstEventLocation(date);
        String meioTransporte = user.getFirstEventTransportation(date);

        // Get duration of travel to event
		DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);
		maps.printDistanceMatrix(matrix, meioTransporte);

		// {hours, minutes, seconds}
		int[] duration = TimeConverter.secondsToHMS(maps.getDuration(matrix).inSeconds);
		int[] start = TimeConverter.dateTimeToHMS(user.getFirstEventStart(date));
		int[] ready = TimeConverter.stringToHMS(user.getTimeToDress());
		int[] alarm = {0,0,0};

		// set alarm seconds
		alarm[2] = start[2] - duration[2] - ready[2];
		if(alarm[2] < 0) {
			// reduce the number of seconds from the next minute
			alarm[2] = 60 + alarm[2];
			// increase by one the number of minutes of duration
			duration[1] += 1;
		}

		// set alarm minutes
		alarm[1] = start[1] - duration[1] - ready[1];
		if(alarm[1] < 0) {
			// reduce the number of minutes from the next hour
			alarm[1] = 60 + alarm[1];
			// increase by one the number of hours of duration
			duration[0] += 1;
		}

		// set alarm hours
		alarm[0] = start[0] - duration[0] - ready[0];
		if(alarm[0] < 0) {
			alarm[0] = 24 + alarm[0];
		}

		TimeConverter.printHMS("[ALARM]", alarm);
	}

	public void storePastEvents() throws IOException {
		maps.storePastEvents();
	}

}

