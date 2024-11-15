package com.romy.prime.organization.dvo;

import com.romy.prime.common.dvo.AuditDvo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * packageName    : com.romy.prime.organization.dvo
 * fileName       : OrgUserDvo
 * author         : 김새롬이
 * date           : 2024-10-14
 * description    : 사용자 dvo
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-14        김새롬이       최초 생성
 */
@Getter
@Setter
public class OrgUserDvo extends AuditDvo {

    // 사원번호
    private String empNo;
    // 성명
    private String empNm;
    // 부서코드
    private String deptCd;
    // 비밀번호
    private String password;
    // 권한ID
    private String roleId;
    // 사용여부
    private byte useYn;
    // 잠금여부
    private byte lockYn;
    // 비밀번호초기화여부
    private byte pwInitYn;
    // 비밀번호오류횟수
    private short pwErrCnt;
    // 비고
    private String remark;

}
