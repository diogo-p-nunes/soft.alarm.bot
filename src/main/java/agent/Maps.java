package agent;

import java.util.ArrayList;

public class Maps {
	
	ArrayList<Evento> eventos;
	
	public Maps () {
		//Scanner sc = new Scanner(source);
		
		eventos = new ArrayList<Evento>();
	}
	
	public float getTimeToEvent(String origem, String destino, MeioTransporte meioTransporte) {
		if (origem == null && destino == null) {
			return -1;
		}
		for (Evento evento : eventos) {
			if (evento.getDestino().equals(destino) && evento.getOrigem().equals(origem) && evento.getMeio_transporte().equals(meioTransporte)) {
				return evento.getDuracao();
			}
		}
		return -1;
	}

}
