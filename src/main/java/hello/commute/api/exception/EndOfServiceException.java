package hello.commute.api.exception;

public class EndOfServiceException extends RuntimeException{

    public EndOfServiceException(String message) {
        super(message);
    }
}
