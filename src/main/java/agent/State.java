package agent;

import user.Evento;

public class State {
	
	private int[] time_to_get_ready;
	private Evento evento;
	
	public State (Evento evento, int[] time_to_get_ready) {
		this.evento = evento;
		this.time_to_get_ready = time_to_get_ready;
	}

	public int[] getTime_to_get_ready() {
		return time_to_get_ready;
	}

	public void setTime_to_get_ready(int[] time_to_get_ready) {
		this.time_to_get_ready = time_to_get_ready;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

}
