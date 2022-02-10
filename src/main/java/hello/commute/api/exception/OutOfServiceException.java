package hello.commute.api.exception;

public class OutOfServiceException extends RuntimeException{

    public OutOfServiceException(String message) {
        super(message);
    }
}
