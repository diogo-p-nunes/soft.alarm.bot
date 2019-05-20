package agent.qlearning;

import agent.Evento;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		else {
			State s = (State) obj;
			return s.getTime_to_get_ready()[0] == this.getTime_to_get_ready()[0] &&
					s.getTime_to_get_ready()[1] == this.getTime_to_get_ready()[1] &&
					s.getTime_to_get_ready()[2] == this.getTime_to_get_ready()[2] &&
					s.getEvento().equals(this.evento);
		}
	}
}
