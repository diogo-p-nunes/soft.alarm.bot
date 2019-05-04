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
	
	public String setAlarm(String date) throws NoEventException, IOException, GetTimeToEventException {

		// Set date's first event data
		String origem = user.getUserLocation();
		String destino = user.getFirstEventLocation(date);
        String meioTransporte = user.getFirstEventTransportation(date);

        // Get duration of travel to event
		DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);

		maps.printDistanceMatrix(matrix);

		// TODO: java.lang.ArrayIndexOutOfBoundsException: 2 at agent.Agent.setAlarm(Agent.java:42) at Main.main(Main.java:13)
		/*
		//ha uma cena para o transito
		String duracao = matrix.rows[0].elements[0].duration.toString();
		String[] array = duracao.split(" ");
		String line = user.getFirstEventStart(date);
		String[] str = user.getTimeToDress().split(":");
		
		int minutos = Integer.parseInt(line.substring(11, 13)) - Integer.parseInt(array[0]) - Integer.parseInt(str[0]);
		minutos *= 60;
		minutos = minutos + Integer.parseInt(line.substring(14, 16)) - Integer.parseInt(array[2]) - Integer.parseInt(str[1]);
		
		return (minutos / 60) + ":" + (minutos % 60);
		*/
		return "ALARM";
	}

	public void storePastEvents() throws IOException {
		maps.storePastEvents();
	}

}

