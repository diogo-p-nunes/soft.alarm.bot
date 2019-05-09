package exception;

public class GetTimeToEventException extends Exception {
    public GetTimeToEventException() {
        super("[EXCEPTION] Destination or source badly specified.");
    }
}
