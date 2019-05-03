package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Maps {
	
	ArrayList<Evento> eventos;
	
	public Maps () {
		//Scanner sc = new Scanner(source);
		
		eventos = new ArrayList<Evento>();
	}
	
	public float getTimeToEvent(String origem, String destino) {
		if (origem == null && destino == null) {
			return -1;
		}
		return 0;
	}

}
