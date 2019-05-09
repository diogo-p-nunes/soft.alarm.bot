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
	
	public String setAlarm(String date) throws IOException {

		int[] alarm = {0,0,0};

		try {
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


			// set alarm seconds
			int[] timeDependency = subtractTime(start[2], duration[2], 60);
			alarm[2] = timeDependency[0];
			duration[1] += timeDependency[1];

			timeDependency = subtractTime(alarm[2], ready[2], 60);
			alarm[2] = timeDependency[0];
			ready[1] += timeDependency[1];

			// set alarm minutes
			timeDependency = subtractTime(start[1], duration[1], 60);
			alarm[1] = timeDependency[0];
			duration[0] += timeDependency[1];

			timeDependency = subtractTime(alarm[1], ready[1], 60);
			alarm[1] = timeDependency[0];
			ready[0] += timeDependency[1];

			// set alarm hours
			timeDependency = subtractTime(start[0], duration[0], 24);
			alarm[0] = timeDependency[0];

			timeDependency = subtractTime(alarm[0], ready[0], 24);
			alarm[0] = timeDependency[0];

		}
		catch (NoEventException | GetTimeToEventException e) {
			// there is no event on that day, so set the alarm to the maximum wake up time
			System.out.println(e.getMessage());
			alarm = TimeConverter.stringToHMS(user.getMaxWakeUp());
		}

		return TimeConverter.HMStoString(alarm);
	}

	public void storePastEvents() throws IOException {
		maps.storePastEvents();
	}


	private int[] subtractTime(int from, int amount, int max) {
		int dependency = 0;
		from = from - amount;
		if (from < 0) {
			from = max + from;
			dependency++;
		}
		int[] res = {from, dependency};
		return res;
	}

}

