package br.com.balaopreto.config.exception;


import br.com.balaopreto.config.dto.ErrorExceptionResponseDto;
import br.com.balaopreto.domain.exception.BaseException;
import br.com.balaopreto.domain.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorExceptionResponseDto> handlerCustomException(CustomException e){
        LOGGER.error("CustomExceprion: {}", e.getMessage(), e);
        ErrorExceptionResponseDto response = new ErrorExceptionResponseDto(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorExceptionResponseDto> handlerBaseException(BaseException e){
        LOGGER.error("BaseException: {}", e.getMessage(), e);
        ErrorExceptionResponseDto response = new ErrorExceptionResponseDto(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorExceptionResponseDto> handlerException(Exception e){
        LOGGER.error("Execption: {}", e.getMessage(), e);
        ErrorExceptionResponseDto response = new ErrorExceptionResponseDto(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}