package com.romy.prime.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.romy.prime.common.exception
 * fileName       : ErrorElement
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : Error Element
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@Getter
@Setter
public class ErrorElement {

    private String code;
    private String details;
    private String message;

    public ErrorElement(ErrorElement element) {
        this(element.getCode(), element.getDetails(), element.getMessage());
    }

    public ErrorElement(String code, String details, String message) {
        this.code = code;
        this.details = details;
        this.message = message;
    }

    public ErrorElement(HttpStatus status) {
        this.code = status.name();
        this.details = HttpStatus.Series.valueOf(status.value()).name();
        this.message = status.getReasonPhrase();
    }

}
