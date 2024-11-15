package com.romy.prime.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.romy.prime.common.response
 * fileName       : SuccessResponse
 * author         : 김새롬이
 * date           : 2024-10-08
 * description    : 공통 Success Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        김새롬이       최초 생성
 */
@Getter
@Setter
public class SuccessResponse<T> extends RestResponse {

    private T data;

    public SuccessResponse(String uri, T data) {
        super(uri);
        this.data = data;
    }

}
