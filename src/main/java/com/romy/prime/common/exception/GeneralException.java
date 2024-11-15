package com.romy.prime.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.romy.prime.common.utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.NestedRuntimeException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.romy.prime.common.exception
 * fileName       : GeneralException
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : 시스템 공통 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@Getter
@Setter
public class GeneralException extends NestedRuntimeException {

    protected transient ArrayList<ErrorElement> errors = new ArrayList<>();

    @JsonIgnore
    private ErrorElement errorElement;

    protected GeneralException(String code, String details, String messageKey, String message,
                               Throwable e) {
        super(message, e);

        String convertMsg = MessageUtils.getMessage(message);
        message = StringUtils.hasText(convertMsg) ? convertMsg : message;

        String internalMsg = (e != null && !StringUtils.hasText(message)) ? e.getMessage() : message;

        this.errorElement = new ErrorElement(code, details, messageKey);
        this.addErrorElement(internalMsg);
    }

    protected GeneralException(String code, String details, String messageKey, Object[] args,
                               String message, Throwable e) {
        super(message, e);

        String convertMsg = MessageUtils.getMessage(messageKey, args);
        if (!StringUtils.hasText(convertMsg)) {
            String tempMsg = MessageUtils.getMessage(message);
            convertMsg = StringUtils.hasText(tempMsg) ? tempMsg : message;
        }

        String internalMsg = (e != null && !StringUtils.hasText(convertMsg)) ? e.getMessage() : convertMsg;

        this.errorElement = new ErrorElement(code, details, messageKey);
        this.addErrorElement(internalMsg);

    }

    public void addErrorElement(String message) {
        ErrorElement element = new ErrorElement(this.errorElement);
        element.setMessage(message);

        this.errors.add(element);
    }

    public List<ErrorElement> getErrors() {
        return this.errors;
    }

}
