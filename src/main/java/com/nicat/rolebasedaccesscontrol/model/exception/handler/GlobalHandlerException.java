package com.nicat.rolebasedaccesscontrol.model.exception.handler;


import com.nicat.rolebasedaccesscontrol.model.exception.AlreadyExistException;
import com.nicat.rolebasedaccesscontrol.model.exception.ForbiddenException;
import com.nicat.rolebasedaccesscontrol.model.exception.NotFoundException;
import com.nicat.rolebasedaccesscontrol.model.exception.UnauthorizedException;
import com.nicat.rolebasedaccesscontrol.model.exception.records.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.NotActiveException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put("error", "Invalid input");
            }
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ExceptionMessage handleForbiddenException(ForbiddenException ex) {
        return new ExceptionMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotActiveException.class)
    public ExceptionMessage handleResourceNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        return new ExceptionMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public ExceptionMessage handleAlreadyExistException(AlreadyExistException ex) {
        log.error(ex.getMessage());
        return new ExceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage handleGenericException(RuntimeException ex) {
        return new ExceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionMessage handleUnauthorizedException(UnauthorizedException ex) {
        return new ExceptionMessage(ex.getMessage());
    }
}