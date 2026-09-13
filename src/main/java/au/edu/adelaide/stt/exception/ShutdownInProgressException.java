package au.edu.adelaide.stt.exception;

public class ShutdownInProgressException
        extends RuntimeException {

    public ShutdownInProgressException() {

        super(
        	"Graceful shutdown is already in progress."
        );
    }
}