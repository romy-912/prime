package com.romy.prime.common.exception;

import org.springframework.http.HttpStatus;

/**
 * packageName    : com.romy.prime.common.exception
 * fileName       : ValidationException
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : API 파라미터 Valid Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
public class ValidationException extends GeneralException {

    protected static final String DEFAULT_CODE = HttpStatus.BAD_REQUEST.name();
    protected static final String DEFAULT_DETAILS = HttpStatus.BAD_REQUEST.series().name();
    protected static final String DEFAULT_MESSAGE_KEY = "error.invalid.message";

    public ValidationException() {
        this(null, null);
    }

    public ValidationException(String message) {
        this(message, null);
    }

    public ValidationException(String message, Throwable e) {
        super(DEFAULT_CODE, DEFAULT_DETAILS, DEFAULT_MESSAGE_KEY, message, e);
    }
}
