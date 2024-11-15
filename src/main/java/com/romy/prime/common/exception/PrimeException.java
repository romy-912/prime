package com.romy.prime.common.exception;

/**
 * packageName    : com.romy.prime.common.exception
 * fileName       : PrimeException
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : 시스템 비즈니스 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
public class PrimeException extends GeneralException {

    protected static final String DEFAULT_CODE = "499";
    protected static final String DEFAULT_DETAILS = "BIZ_EXCEPTION";
    protected static final String DEFAULT_MESSAGE_KEY = "error.biz.message";

    public PrimeException() {
        super(DEFAULT_CODE, DEFAULT_DETAILS, DEFAULT_MESSAGE_KEY, null, null);
    }

    public PrimeException(String message) {
        super(DEFAULT_CODE, DEFAULT_DETAILS, DEFAULT_MESSAGE_KEY, message, null);
    }

    public PrimeException(String message, Object[] args) {
        super(DEFAULT_CODE, DEFAULT_DETAILS, message, args, "", null);
    }

}
