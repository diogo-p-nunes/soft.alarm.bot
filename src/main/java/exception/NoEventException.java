package exception;

public class NoEventException extends Exception {
    public NoEventException() {
        super("[EXCEPTION] : No event on specified date.");
    }
}
