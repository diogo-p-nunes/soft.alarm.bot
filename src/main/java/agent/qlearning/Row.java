package agent.qlearning;


import agent.Agent;

import java.util.Random;

public class Row {

    private double reduce_dress, keep_dress, increase_dress;
    private int VALUE = 10;
    private State state;
    private int freshness;
    private int executed_action;

    public Row(State state, int freshness) {
        reduce_dress = 0;
        keep_dress = 0;
        increase_dress = 0;
        this.state = state;
        this.freshness = freshness;
    }

    public int getExecuted_action() {
        return executed_action;
    }

    public void setExecuted_action(int executed_action) {
        this.executed_action = executed_action;
    }

    public int getFreshness() {
        return freshness;
    }

    public void setFreshness(int freshness) {
        this.freshness = freshness;
    }

    public double getReduce_dress() {
        return reduce_dress;
    }

    public void setReduce_dress(double reduce_dress) {
        this.reduce_dress = reduce_dress;
    }

    public double getKeep_dress() {
        return keep_dress;
    }

    public void setKeep_dress(double keep_dress) {
        this.keep_dress = keep_dress;
    }

    public double getIncrease_dress() {
        return increase_dress;
    }

    public void setIncrease_dress(double increase_dress) {
        this.increase_dress = increase_dress;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State executeRandomAction() {
        Random r = new Random(42);
        double rand = r.nextDouble();
        if (rand >= 0 && rand < (1.0 / 3)) {
            return new State(this.state.getEvento(), executeReduction());
        }
        else if (rand >= (1.0 / 3) && rand < (2.0 / 3)) {
            executed_action = 2;
            return this.state;
        }
        else {
            return new State(this.state.getEvento(), executeIncrement());
        }
    }

    private int[] executeIncrement() {
        int[] ready = this.state.getTime_to_get_ready().clone();

        // set alarm minutes
        int[] timeDependency = Agent.subtractTime(ready[1], -VALUE, 60);
        ready[1] = timeDependency[0];
        int duration = timeDependency[1];

        // set alarm hours
        timeDependency = Agent.subtractTime(ready[0], -duration, 24);
        ready[0] = timeDependency[0];

        executed_action = 3;
        return ready;
    }

    private int[] executeReduction () {
        int[] ready = this.state.getTime_to_get_ready().clone();

        // set alarm minutes
        int[] timeDependency = Agent.subtractTime(ready[1], VALUE, 60);
        ready[1] = timeDependency[0];
        int duration = timeDependency[1];

        // set alarm hours
        timeDependency = Agent.subtractTime(ready[0], duration, 24);
        ready[0] = timeDependency[0];

        executed_action = 1;
        return ready;
    }

    public State executeMaxAction() {
        //keep
        if (keep_dress >= reduce_dress && keep_dress >= increase_dress) {
            executed_action = 2;
            return this.state;
        }
        //reduce
        else if (reduce_dress >= keep_dress && reduce_dress >= increase_dress) {
            return new State(this.state.getEvento(), executeReduction());
        }
        //increase
        else if (increase_dress >= reduce_dress && increase_dress >= keep_dress) {
            return new State(this.state.getEvento(), executeIncrement());
        }
        return this.state;
    }

    @Override
    public String toString() {
        String state = getState().toString();
        String action_utilities = reduce_dress + "\t " + keep_dress + "\t " + increase_dress;
        return state + "\t " + action_utilities + "\t " + getFreshness();
    }
}

