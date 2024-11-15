package com.romy.prime.common.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * packageName    : com.romy.prime.common.response
 * fileName       : SaveProcessResponse
 * author         : 김새롬이
 * date           : 2024-10-04
 * description    : CUD Action에 대한 공통 Response 객체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-04        김새롬이       최초 생성
 */
@Schema(name = "처리 Res")
public record SaveProcessResponse(
        @Schema(title = "처리건수", requiredMode = Schema.RequiredMode.REQUIRED, type = "number")
        int processCount,
        @Schema(title = "PK 데이터 값")
        String primaryValue
) {
}
