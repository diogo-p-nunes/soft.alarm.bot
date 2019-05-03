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

	public Agent() throws IOException, GeneralSecurityException {
		user = new UserInfo();
		maps = new Maps();
	}
	
	public String setAlarm() throws NoEventException, IOException, GetTimeToEventException {
        String date = "2019-05-02T00:00:00Z";
		String origem = user.getUserLocation();	
		String destino = user.getFirstEventLocation(date);
        String meioTransporte = user.getFirstEventTransportation(date);
        
		DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);
		//ha uma cena para o transito
		String duracao = matrix.rows[0].elements[0].duration.toString();
		String[] array = duracao.split(" ");
		String line = user.getFirstEventStart(date);
		String[] str = user.getTimeToDress().split(":");
		
		int minutos = Integer.parseInt(line.substring(11, 13)) - Integer.parseInt(array[0]) - Integer.parseInt(str[0]);
		minutos *= 60;
		minutos = minutos + Integer.parseInt(line.substring(14, 16)) - Integer.parseInt(array[2]) - Integer.parseInt(str[1]);
		
		return ((int)minutos / 60) + ":" + ((int)minutos % 60);
	}

}

