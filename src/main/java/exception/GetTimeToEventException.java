package exception;

public class GetTimeToEventException extends Exception {
    public GetTimeToEventException() {
        super("[EXCEPTION] : Location or time badly specified.");
    }
}
