package au.edu.adelaide.stt.exception;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import au.edu.adelaide.stt.model.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    GlobalExceptionHandler.class
            );

    @ExceptionHandler(
            ShutdownInProgressException.class
    )
    public ResponseEntity<ErrorResponse>
            handleShutdownInProgress(
                    ShutdownInProgressException exception,
                    HttpServletRequest request
            ) {

        LOGGER.warn(
                "Shutdown request rejected for path {} because shutdown is already in progress",
                request.getRequestURI()
        );

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        409,
                        "Conflict",
                        exception.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(
                        HttpStatus.CONFLICT
                )
                .body(response);
    }

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<ErrorResponse>
            handleBadRequest(
                    IllegalArgumentException exception,
                    HttpServletRequest request
            ) {

        LOGGER.warn(
                "Bad request on path {}: {}",
                request.getRequestURI(),
                exception.getClass()
                        .getSimpleName()
        );

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        400,
                        "Bad Request",
                        exception.getMessage(),
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(response);
    }

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<ErrorResponse>
            handleUnexpectedError(
                    Exception exception,
                    HttpServletRequest request
            ) {

        LOGGER.error(
                "Unexpected server error on path {}: {}",
                request.getRequestURI(),
                exception.getClass()
                        .getSimpleName()
        );

        ErrorResponse response =
                new ErrorResponse(
                        Instant.now(),
                        500,
                        "Internal Server Error",
                        "An unexpected server error occurred.",
                        request.getRequestURI()
                );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }
}