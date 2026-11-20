package tp;

public class EndReadLoopException extends RuntimeException {
    public EndReadLoopException() {
        super();
    }

    public EndReadLoopException(String message) {
        super(message);
    }

    public EndReadLoopException(String message, Throwable cause) {
        super(message, cause);
    }
}
