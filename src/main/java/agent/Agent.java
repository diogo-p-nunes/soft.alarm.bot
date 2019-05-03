package agent;

import java.io.IOException;
import java.security.GeneralSecurityException;

import exception.NoEventException;
import user.UserInfo;

public class Agent {
	
	private UserInfo user;
	private Maps maps;

	public Agent() throws IOException, GeneralSecurityException {
		user = new UserInfo();
		maps = new Maps();
	}
	
	public String setAlarm() throws NoEventException, IOException {
		String line = user.getFirstEventStart("2019-05-02T00:00:00Z");
		line = user.getFirstEventLocation("2019-05-02T00:00:00Z");
		//float duracao = maps.getTimeToEvent(origem, destino, meioTransporte);
		return null;
	}

}

