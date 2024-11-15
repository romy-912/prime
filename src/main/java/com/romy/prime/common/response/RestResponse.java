package com.romy.prime.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.romy.prime.common.response
 * fileName       : RestResponse
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : 공통 Rest Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@Getter
@Setter
public class RestResponse {

    protected String uri;

    protected RestResponse(String uri) {
        this.uri = uri;
    }
}
