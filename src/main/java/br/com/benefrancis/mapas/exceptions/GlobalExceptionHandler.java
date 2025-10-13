package br.com.benefrancis.mapas.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ApiFetchException.class)
    public ResponseEntity<String> handleApiFetchException(ApiFetchException ex) {
        // Log the exception
        logger.error("API Fetch Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Error fetching data from API: " + ex.getMessage());
    }

    @ExceptionHandler(DataParseException.class)
    public ResponseEntity<String> handleDataParseException(DataParseException ex) {
        // Log the exception
        logger.error("Data Parse Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error parsing data: " + ex.getMessage());
    }

    @ExceptionHandler(DataPersistenceException.class)
    public ResponseEntity<String> handleDataPersistenceException(DataPersistenceException ex) {
        // Log the exception
        logger.error("Data Persistence Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error persisting data: " + ex.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        // Log the exception
        logger.error("Generic Runtime Exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}