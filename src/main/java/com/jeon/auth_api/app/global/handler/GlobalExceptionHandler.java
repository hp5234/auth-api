package com.jeon.auth_api.app.global.handler;

import com.jeon.auth_api.app.global.dto.CommonErrorRespDto;
import com.jeon.auth_api.app.global.exception.CustomException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorRespDto> handleCustomExceptionException(CustomException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonErrorRespDto.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorRespDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonErrorRespDto.of("VALIDATION_ERROR", errorMessage));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<CommonErrorRespDto> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        String errorMessage = e.getMessage();
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonErrorRespDto.of("VALIDATION_ERROR", errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonErrorRespDto> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonErrorRespDto.of("VALIDATION_ERROR", errorMessage));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonErrorRespDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String errorMessage = "허용되지 않은 HTTP 메서드입니다.";

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CommonErrorRespDto.of("METHOD_NOT_ALLOWED", errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorRespDto> handleException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonErrorRespDto.of("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CommonErrorRespDto> handleThrowable(Throwable e) {
        log.error("Unexpected Throwable: {}", e.getMessage(), e);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonErrorRespDto.of("INTERNAL_SERVER_ERROR", "알 수 없는 오류가 발생했습니다."));
    }
}
