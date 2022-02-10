package hello.commute.api.exception;

public class TooCloseException extends RuntimeException{

    public TooCloseException(String message) {
        super(message);
    }
}
