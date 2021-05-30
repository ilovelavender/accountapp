package ru.olegr.accountapp.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.olegr.accountapp.model.dto.ArgumentNotValidDetails;
import ru.olegr.accountapp.model.dto.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlingController {

    private static final String GENERAL_VALIDATION_MESSAGE = "Found %s validation errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ArgumentNotValidDetails> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exe,
        HttpServletRequest httpServletRequest
    ) {
        String message = String.format(GENERAL_VALIDATION_MESSAGE, exe.getErrorCount());

        List<FieldError> fieldErrors = exe.getFieldErrors().stream().map(err ->
            new FieldError(err.getDefaultMessage(), err.getField(), err.getRejectedValue())
        ).collect(Collectors.toList());

        return ResponseEntity.ok(new ArgumentNotValidDetails(httpServletRequest.getRequestURI(), new Date(), message, fieldErrors));
    }
}
