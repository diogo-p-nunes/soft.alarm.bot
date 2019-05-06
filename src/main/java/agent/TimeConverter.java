package agent;

public class TimeConverter {
    public static int[] stringToHMS(String timeToDress) {
        int hours = Integer.parseInt(timeToDress.substring(0,2));
        int mins = Integer.parseInt(timeToDress.substring(3,5));
        int seconds = Integer.parseInt(timeToDress.substring(6,8));
        int[] res = {hours, mins, seconds};
        printHMS("[DRESS]", res);
        return res;
    }

    public static int[] dateTimeToHMS(String firstEventStart) {
        int hours = Integer.parseInt(firstEventStart.substring(11,13));
        int mins = Integer.parseInt(firstEventStart.substring(14,16));
        int seconds = Integer.parseInt(firstEventStart.substring(17,19));
        int[] res = {hours, mins, seconds};
        printHMS("[START]", res);
        return res;
    }

    public static int[] secondsToHMS(long totalSeconds) {
        int mins = (int) Math.floor((totalSeconds / 60.0) % 60);
        int hours = (int) Math.floor(totalSeconds / 3600.0);
        int seconds = (int) totalSeconds % 60;
        int[] res = {hours, mins, seconds};
        printHMS("[DURATION]", res);
        return res;
    }

    public static void printHMS(String type, int[] hms) {
        System.out.print("\t" + type + " ");
        for(int i : hms) {
            System.out.print(i + ":");
        }
        System.out.println();
    }
}
