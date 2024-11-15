package com.romy.prime.common.response;

import com.romy.prime.common.exception.ErrorElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * packageName    : com.romy.prime.common.response
 * fileName       : ErrorResponse
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : 공통 Error Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@Getter
@Setter
public class ErrorResponse extends RestResponse {

    private List<ErrorElement> errors = new ArrayList<>();

    public ErrorResponse(String uri) {
        super(uri);
    }

    public ErrorResponse(String uri, ErrorElement... errors) {
        super(uri);
        Collections.addAll(this.errors, errors);
    }

}
