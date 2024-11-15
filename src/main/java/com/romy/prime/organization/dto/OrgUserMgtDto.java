package com.romy.prime.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * packageName    : com.romy.prime.organization.dto
 * fileName       : OrgUserMgtDto
 * author         : 김새롬이
 * date           : 2024-10-22
 * description    : 사용자 관리 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-22        김새롬이       최초 생성
 */
public class OrgUserMgtDto {

    @Schema(name = "사용자관리 조회 Req")
    public record SearchReq(
            @Schema(title = "사원번호")
            String empNo,
            @Schema(title = "성명")
            String empNm,
            @Schema(title = "부서명")
            String deptNm,
            @Schema(title = "사용여부", description = "사용 : 1, 미사용 : 0", type = "number", allowableValues = {"0", "1"})
            Byte useYn
    ){}

    @Schema(name = "사용자관리 조회 Res")
    public record SearchRes(
            @Schema(title = "사원번호")
            String empNo,
            @Schema(title = "성명")
            String empNm,
            @Schema(title = "부서코드")
            String deptCd,
            @Schema(title = "권한ID")
            String roleId,
            @Schema(title = "사용여부", description = "사용 : 1, 미사용 : 0", type = "number", allowableValues = {"0", "1"})
            byte useYn,
            @Schema(title = "잠금여부", description = "잠금 : 1, 해제 : 0", type = "number",
                    allowableValues = {"0", "1"})
            byte lockYn,
            @Schema(title = "비밀번호 초기화 여부", description = "초기화 : 1, 해제 : 0", type = "number",
                    allowableValues = {"0", "1"})
            byte pwInitYn,
            @Schema(title = "비밀번호 오류횟수", type = "number")
            short pwErrCnt,
            @Schema(title = "비고")
            String remark
    ){}

    @Schema(name = "사용자관리 저장 Req")
    public record SaveReq(
            // row 상태 (신규 : I, 수정 : U)
            @NotBlank
            @Size(max = 1)
            @Schema(title = "row 상태", description = "신규 : I, 수정 : U", requiredMode =
                    Schema.RequiredMode.REQUIRED, allowableValues = {"I", "U"})
            String rowStatus,
            @NotBlank
            @Size(max = 10)
            @Schema(title = "사원번호", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 10)
            String empNo,
            @NotBlank
            @Size(max = 30)
            @Schema(title = "성명", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 30)
            String empNm,
            @Size(max = 10)
            @Schema(title = "부서코드", maxLength = 10)
            String deptCd,
            @Size(max = 10)
            @Schema(title = "권한ID", maxLength = 10)
            String roleId,
            @Schema(title = "사용여부", description = "사용 : 1, 미사용 : 0", type = "number",
                    allowableValues = {"0", "1"})
            byte useYn,
            @Min(0L)
            @Max(999L)
            @Schema(title = "비밀번호 오류횟수", type = "number", minimum = "0", maximum = "999")
            short pwErrCnt,
            @Size(max = 4000)
            @Schema(title = "비고", maxLength = 4000)
            String remark
    ){}

}
