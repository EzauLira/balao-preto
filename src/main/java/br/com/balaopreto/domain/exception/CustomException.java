package br.com.balaopreto.domain.exception;

public class CustomException extends RuntimeException {

    public CustomException(String message){
        super(message);
    }
}