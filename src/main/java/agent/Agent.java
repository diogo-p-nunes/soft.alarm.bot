package agent;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Random;

import agent.qlearning.Row;
import agent.qlearning.State;
import com.google.maps.model.DistanceMatrix;

import exception.GetTimeToEventException;
import exception.NoEventException;
import user.UserInfo;

public class Agent {
	
	private UserInfo user;
	private Maps maps;
	private HashMap<State, Row> q_table;
	private State curr_state, new_state;
	private int action;
	private double learningRate = 0.8;
	private double gamma = 0.4;
	private double EXPLORATION = 0.3;

	public Agent() throws IOException, GeneralSecurityException, ClassNotFoundException, NoEventException, GetTimeToEventException {
		user = new UserInfo();
		maps = new Maps();
		q_table = new HashMap<>();
	}

	public String processData(String date) throws IOException {

		int[] alarm;

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

			Evento evento = new Evento(origem,destino, meioTransporte, matrix);
			ready = check_existing_ready(ready, evento);

			curr_state = new State(evento,ready);

			Random r = new Random();
			new_state = executeAction(curr_state,r.nextDouble());

			//int[] aux = new_state.getTime_to_get_ready().clone();
			alarm = setAlarm(start, duration, new_state.getTime_to_get_ready().clone());
		}
		catch (NoEventException | GetTimeToEventException e) {
			// there is no event on that day, so set the alarm to the maximum wake up time
			System.out.println(e.getMessage());
			alarm = TimeConverter.stringToHMS(user.getMaxWakeUp());
		}

		return TimeConverter.HMStoString(alarm);
	}

	public void processReward (int reward) {
		if (curr_state == null && new_state == null) {
			return;
		}
		Row row = getRow(curr_state,0);
		Row row_next = getRow(new_state, row.getFreshness()+1);
		double melhor;
		if (action == 1) {
			melhor = max_action(row_next, row.getReduce_dress());
			row.setReduce_dress(row.getReduce_dress() + learningRate * (reward + gamma * melhor));
		}
		else if (action == 2) {
			melhor = max_action(row_next, row.getKeep_dress());
			row.setKeep_dress(row.getKeep_dress() + learningRate * (reward + gamma * melhor));
		}
		else if (action == 3) {
			melhor = max_action(row_next, row.getIncrease_dress());
			row.setIncrease_dress(row.getIncrease_dress() + learningRate * (reward + gamma * melhor));
		}

		EXPLORATION *= 0.9;
	}

	private double max_action(Row row_next, double d) {
		double[] array = new double[3];
		array[0] = row_next.getReduce_dress() - d;
		array[1] = row_next.getKeep_dress() - d;
		array[2] = row_next.getIncrease_dress() - d;

		double best = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > best){
				best = array[i];
			}
		}
		return best;
	}

	private int[] check_existing_ready(int[] ready, Evento evento) {
		int freshness = 0;
		State maxFreshness = null;
		for (Row r: q_table.values()) {
			if (r.getState().getEvento().equals(evento) && r.getFreshness() > freshness) {
				freshness = r.getFreshness();
				maxFreshness = r.getState();
			}
		}
		if (maxFreshness == null) {
			return ready;
		}
		return maxFreshness.getTime_to_get_ready();
	}

	private Row getRow (State state, int freshness) {
		Row row = null;
		for (State s : q_table.keySet()) {
			if(s.equals(state)) {
				row = q_table.get(s);
			}
		}
		if (row == null) {
			row = new Row(state, freshness);
			q_table.put(state,row);
		}
		return row;
	}

	private State executeAction(State curr_state, double random) {
		Row row = getRow(curr_state,0);

		System.out.println(EXPLORATION);
		State new_state;
		if (random < EXPLORATION) {
			System.out.println("------------> Tine to explore!!!!!!!!!");
			new_state = row.executeRandomAction();
		}
		else {
			new_state = row.executeMaxAction();
		}
		action = row.getExecuted_action();
		return new_state;
	}

	private int[] setAlarm (int[] start, int[] duration, int[] ready) {
		int[] alarm = {0,0,0};

		TimeConverter.printHMS("[DRESS]", ready);

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

		return alarm;
	}

	public void storePastEvents() throws IOException {
		maps.storePastEvents();
	}


	public static int[] subtractTime(int from, int amount, int max) {
		int dependency = 0;
		from = from - amount;
		if (from < 0) {
			from = max + from;
			dependency++;
		}
		else if (from >= max) {
			from = from - max;
			dependency++;
		}
		int[] res = {from, dependency};
		return res;
	}

}

