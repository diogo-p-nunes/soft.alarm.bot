package user;

public class Feedback {

    private String timeToDress;
    private String maxWakeUp;

    public Feedback() {
        this.timeToDress = "00:40:00";
        this.maxWakeUp = "09:00:00";
    }

    public String getTimeToDress() {
        return timeToDress;
    }

    public void setTimeToDress(String timeToDress) {
        this.timeToDress = timeToDress;
    }

    public String getMaxWakeUp() {
        return maxWakeUp;
    }

    public void setMaxWakeUp(String maxWakeUp) {
        this.maxWakeUp = maxWakeUp;
    }
}
