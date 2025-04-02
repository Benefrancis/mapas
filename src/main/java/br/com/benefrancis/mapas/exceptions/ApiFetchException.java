package br.com.benefrancis.mapas.exceptions;

public class ApiFetchException extends RuntimeException {
    public ApiFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}