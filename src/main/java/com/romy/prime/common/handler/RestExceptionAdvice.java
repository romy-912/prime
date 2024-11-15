package com.romy.prime.common.handler;

import com.romy.prime.common.PrimeConstant;
import com.romy.prime.common.exception.*;
import com.romy.prime.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLTransientException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

/**
 * packageName    : com.romy.prime.common.handler
 * fileName       : RestExceptionAdvice
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : Exception Handler
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@RestControllerAdvice
public class RestExceptionAdvice {

    private ConstraintViolationException e;

    public ErrorResponse errorResponse(HttpServletRequest request, Exception e, HttpStatus status) {

        ErrorResponse errorRes = new ErrorResponse(request.getRequestURI());

        if (e instanceof GeneralException) {
            errorRes.getErrors().addAll(((GeneralException) e).getErrors());
        } else {
            ErrorElement element = new ErrorElement(status);
            element.setMessage(e.getMessage());
            errorRes.getErrors().add(element);
        }

        return errorRes;
    }

    public ErrorResponse errorResponse(HttpServletRequest request, GeneralException e) {

        ErrorResponse errorRes = new ErrorResponse(request.getRequestURI());
        errorRes.getErrors().addAll(e.getErrors());

        return errorRes;
    }

    public ErrorResponse errorResponse(HttpServletRequest request) {

        ErrorResponse errorRes = new ErrorResponse(request.getRequestURI());

        return errorRes;
    }

    @ExceptionHandler({ServletRequestBindingException.class, ValidationException.class,
            MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpServletRequest request, Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e instanceof MethodArgumentNotValidException ex) {
            FieldError fieldError = ex.getFieldError();
            String field = fieldError != null ? fieldError.getField() : "";
            String message =
                    Optional.ofNullable(fieldError).map(FieldError::getDefaultMessage).orElse("");

            String convertMsg = StringUtils.isBlank(field) ? message : "[" + field + "] " + message;

            ErrorResponse errorResponse = this.errorResponse(request);
            ErrorElement element = new ErrorElement(String.valueOf(status.value()), status.name()
                    , convertMsg);
            errorResponse.getErrors().add(element);

            return new ResponseEntity<>(errorResponse, null, status.value());

        } if (e instanceof ConstraintViolationException ex) {

            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            ErrorResponse errorResponse = this.errorResponse(request);

            for (ConstraintViolation<?> violation : violations) {
                String field = "";
                Optional<Path.Node> node =
                        StreamSupport.stream(violation.getPropertyPath().spliterator(), false).reduce((first, second) -> second);
                if (node.isPresent()) {
                    field = node.get().getName();
                }
                String message = violation.getMessage();
                String convertMsg = StringUtils.isBlank(field) ? message : "[" + field + "] " + message;

                ErrorElement element = new ErrorElement(String.valueOf(status.value()), status.name()
                        , convertMsg);
                errorResponse.getErrors().add(element);
            }

            return new ResponseEntity<>(errorResponse, null, status.value());
        }

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(HttpServletRequest request,
                                                              NoHandlerFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpServletRequest request,
                                                                HttpRequestMethodNotSupportedException e) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

    @ExceptionHandler({HttpMediaTypeException.class})
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpServletRequest request,
                                                                     HttpMediaTypeException e) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ErrorResponse> handleReponseStatusException(HttpServletRequest request,
                                                                      ResponseStatusException e) {
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

    @ExceptionHandler({PrimeException.class})
    public ResponseEntity<ErrorResponse> handlePrimeException(HttpServletRequest request,
                                                                 PrimeException e) {
        int status = PrimeConstant.GENERAL_EXCEPTION_STATUS;

        return new ResponseEntity<>(this.errorResponse(request, e), null, status);
    }

    @ExceptionHandler({UnAuthorizationException.class})
    public ResponseEntity<ErrorResponse> handleUnAuthorizationException(HttpServletRequest request,
                                                                        UnAuthorizationException e) {
        int status = HttpStatus.UNAUTHORIZED.value();

        return new ResponseEntity<>(this.errorResponse(request, e), null, status);
    }

    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<ErrorResponse> handleGeneralException(HttpServletRequest request,
                                                                GeneralException e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        return new ResponseEntity<>(this.errorResponse(request, e), null, status);
    }

    @ExceptionHandler({RestClientResponseException.class})
    public ResponseEntity<ErrorResponse> handleRestClientResponseException(HttpServletRequest request,
                                                                           RestClientResponseException e) {
        ErrorResponse errorResponse = this.errorResponse(request);
        int statusCode = e.getStatusCode().value();
        ErrorElement element = new ErrorElement(String.valueOf(statusCode),
                HttpStatus.Series.CLIENT_ERROR.name(), "");

        element.setMessage(e.getMessage());
        errorResponse.getErrors().add(element);

        return new ResponseEntity<>(errorResponse, null, statusCode);
    }

    @ExceptionHandler({PersistenceException.class, SQLTransientException.class,
            DataAccessException.class})
    public ResponseEntity<ErrorResponse> handlePersistenceException(HttpServletRequest request,
                                                                    Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = this.errorResponse(request);
        ErrorElement element = new ErrorElement(status);
        element.setMessage("Database is error.");

        errorResponse.getErrors().add(element);

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(this.errorResponse(request, e, status), status);
    }

}
