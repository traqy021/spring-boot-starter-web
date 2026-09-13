package au.edu.adelaide.stt.exception;

public class ShutdownInProgressException
        extends RuntimeException {

    public ShutdownInProgressException() {

        super(
            "Shutting down is already in progress."
        );
    }
}