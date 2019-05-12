package agent;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.maps.model.DistanceMatrix;

import exception.GetTimeToEventException;
import exception.NoEventException;
import user.Evento;
import user.UserInfo;

public class Agent {
	
	private UserInfo user;
	private Maps maps;
	
	private enum Action {increase_time, decrease_time, maintaine_time};
	private List<Action> actions;
	private double[][] q;
	
	private enum ActionSelection {eGreedy, softMax};
	private enum LearningApproach {QLearning, SARSA};
	
	private ActionSelection actionSelection = ActionSelection.eGreedy;
	private LearningApproach learningApproach = LearningApproach.QLearning;
	
	int it = 0, total = 10000;
	double discount = 0.9, learningRate = 0.8;
	double epsilon = 0.7, randfactor = 0.05, dec;
	private Random random;
	
	public Evento evento;
	public int[] time_to_get_ready;

	public Agent(String date) throws IOException, GeneralSecurityException, ClassNotFoundException, NoEventException, GetTimeToEventException {
		user = new UserInfo();
		maps = new Maps();
		
		String origem = user.getUserLocation();
		String destino = user.getFirstEventLocation(date);
		String meioTransporte = user.getFirstEventTransportation(date);

		// Get duration of travel to event
		DistanceMatrix matrix = maps.getTimeToEvent(origem, destino, meioTransporte);
		this.evento = new Evento(origem, destino, meioTransporte, matrix);
		this.time_to_get_ready = TimeConverter.stringToHMS(user.getTimeToDress());
		
		initQFunction();
	}
	
	public void agentDecision() {
		State originalState = getState(evento, time_to_get_ready);
		Action originalAction = selectAction();
		execute(originalAction);

		//proactiveDecision(); /* DBI */
		//reactiveDecision();
		learningDecision(originalState,originalAction); /* RL */
	}		

	/************************
	 **** A: Q-learning ***** 
	 ************************/
	
	/* Accesses the state of an agent given its position, direction and cargo */
	public State getState(Evento evento, int[] time_to_get_ready) {
		return new State(evento, time_to_get_ready);
	}
	
	/* Creates the initial Q-value function structure: (x y action) <- 0 */
	public void initQFunction(){
		actions = Arrays.asList(Action.values());
		q = new double[Board.nX*Board.nY*directions*(Board.colors.length+1)][actions.size()];
		dec = (epsilon-0.1)/total;
	}
		
	/* Learns policy up to a certain step and then uses policy to behave */
	public void learningDecision(State originalState, Action originalAction) {
		it++;
		double u = reward(originalState,originalAction);
		double prevq = getQ(originalState,originalAction);
		double predError = 0;
		
		epsilon = Math.max(epsilon-dec,0.05);
		
		switch(learningApproach) {
			case SARSA : 
				Action newAction = selectAction();
				predError = u + discount*getQ(getState(evento, time_to_get_ready), newAction) - prevq; break;
			case QLearning : predError = u + discount*getMaxQ(getState(evento, time_to_get_ready)) - prevq; break;
		}
		setQ(originalState, originalAction, prevq+(learningRate * predError));
		//if(it%1000==0) System.out.println("e="+epsilon+"\n"+qToString());
	}
	
	/* Executes action according to the learned policy */
	public void executeQ() {
		if(random.nextDouble()<randfactor) execute(randomAction());
		else execute(getMaxActionQ(getState(evento, time_to_get_ready),availableActions()));
	}
	
	/* Selects action according to e-greedy or soft-max strategies */
	public Action selectAction() {
		epsilon -= dec;
		if(random.nextDouble()<randfactor) return randomAction(); 
		switch(actionSelection) {
			case eGreedy : return eGreedySelection();
			default : return softMax();
		}
	}

	/* Select a random action */
	private Action randomAction() {
		List<Integer> validActions = availableActions(); //index of available actions
		return actions.get(validActions.get(random.nextInt(validActions.size())));
	}

	/* SoftMax action selection */
	private Action softMax() {
		List<Integer> validActions = availableActions(); //index of available actions
		double[] cumulative = new double[validActions.size()];
		cumulative[0]=Math.exp(getQ(getState(evento, time_to_get_ready),actions.get(0))/(epsilon*100.0));
		for(int i=1; i<validActions.size(); i++) 
			cumulative[i]=Math.exp(getQ(getState(evento, time_to_get_ready),actions.get(i))/(epsilon*100.0))+cumulative[i-1];
		double total = cumulative[validActions.size()-1];
		double cut = random.nextDouble()*total;
		for(int i=0; i<validActions.size(); i++) 
			if(cut<=cumulative[i]) return actions.get(validActions.get(i));
		return null;
	}

	/* eGreedy action selection */
	private Action eGreedySelection() {
		List<Integer> validActions = availableActions(); //index of available actions
		if(random.nextDouble()>epsilon) 
			return actions.get(validActions.get(random.nextInt(validActions.size())));
		else return getMaxActionQ(getState(evento, time_to_get_ready),validActions);
	}

	/* Retrieves reward from action */
	private int reward(State state, Action action) {
		switch(action) {
			case increase_time : return 100;
			case decrease_time : return 100;
			case maintaine_time : return 100;
			default : return 0;
		}
	}

	/* Gets the index of maximum Q-value action for a state */
	private Action getMaxActionQ(State state, List<Integer> actionsIndexes) {
		double max = Double.NEGATIVE_INFINITY;
		int maxIndex = -1;
		for(int i : actionsIndexes) {
			double v = q[state][i];
			if(v>max) {
				max = v;
				maxIndex = i;
			}
		}
		return actions.get(maxIndex);
	}
	
	/* Get action with higher likelihood for a given state from q */
	private Action getMaxActionQ(int state) {
		double max = Double.NEGATIVE_INFINITY;
		int maxIndex = -1;
		for(int i=0; i<actions.size(); i++) {
			if(q[state][i]>max) {
				max = q[state][i];
				maxIndex = i;
			}
		}
		return actions.get(maxIndex);
	}

	/* Gets the maximum Q-value action for a state (x y) */
	private double getMaxQ(int state) {
		double max = Double.NEGATIVE_INFINITY;
		for(double v : q[state]) max = Math.max(v, max);
		return max;
	}

	/* Gets the maximum Q-value action for a state (x y) */
	private boolean singleMaxQ(int state) {
		int count = 0;
		double max = getMaxQ(state);
		for(double v : q[state]) if(v==max) count++;
		return count<=1;
	}

	/* Gets the Q-value for a specific state-action pair (x y action) */
	private double getQ(State state, Action action) {
		return q[state][actions.indexOf(action)];
	}

	/* Sets the Q-value for a specific state-action pair (x y action) */
	private void setQ(State state, Action action, double val) {
		q[state][actions.indexOf(action)] = val;
	}

	/* Returns the index of eligible actions */
	private List<Integer> availableActions() {
		List<Integer> res = new ArrayList<Integer>();
		return res;
	}

	
	/* Print state-action q-policy */
	/*
	public String qToString() {
		String res = "";
		for(int j=0; j<Board.nY; j++, res+="\n") { 
			for(int i=0; i<Board.nX; i++, res+=",") {
				for(int k=0, s=Board.colors.length; k<s; k++) {	
					for(int l=0; l<4; l++) {	
						int state = k*(directions*Board.nX*Board.nY)+l*(Board.nX*Board.nY)+i*Board.nY+j;
						if(singleMaxQ(state)) {
							switch(getMaxActionQ(state)) {
								case moveAhead : res+="-"; break;
								case grab : res+="g"; break;
								case drop : res+="d"; break;
								case rotateLeft : res+="<"; break;
								case rotateRight : res+=">"; break;
							}
						} else res+="o";
					}
					if(k<s-1) res+="|";
				}
			}
		}
		return res;
	}
	*/
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
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

