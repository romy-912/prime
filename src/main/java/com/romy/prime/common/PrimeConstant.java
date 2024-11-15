package com.romy.prime.common;

/**
 * packageName    : com.romy.prime.common
 * fileName       : PrimeConstant
 * author         : 김새롬이
 * date           : 2024-10-02
 * description    : 공통 상수처리
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-02        김새롬이       최초 생성
 */
public class PrimeConstant {


    public static final int GENERAL_EXCEPTION_STATUS = 499;

    // row 상태-신규
    public static final String ROW_INSERT = "I";
    // row 상태-수정
    public static final String ROW_UPDATE = "U";

    // 여부
    public static final byte YN_Y = 1;
    public static final byte YN_N = 0;

    /**
     * 메시지 코드 리스트
     */
    // {0} 은(는) 필수 입력 항목입니다.
    public static final String COMMON_REQUIRED_VALUE = "common.required.value";
    // {0} 은(는) 0보다 큰 값을 입력해 주세요.
    public static final String COMMON_POSITIVE_VALUE = "common.positive.value";
    // 입력한 {0}은(는) 이미 존재하는 데이터 입니다. [{1}]
    public static final String COMMON_DUPLICATE_VALUE = "common.duplicate.value";
    // {0}은(는) 유효하지 않은 데이터 입니다. [{1}]
    public static final String COMMON_INVALID_VALUE = "common.invalid.value";
    // 사용자가 없습니다. [아이디: {0}]
    public static final String AUTH_USER_EMPTY = "auth.user.empty";
    // 잘못된 접근입니다.
    public static final String AUTH_INVALID_ACCESS = "auth.invalid.access";
    // 계정이 잠겼습니다. 관리자에게 문의해 주세요.
    public static final String AUTH_USER_LOCK = "auth.user.lock";
    // 비밀번호가 일치하지 않습니다.
    public static final String AUTH_INVALID_PASSWD = "auth.invalid.passwd";

    /**
     * 필드 리스트
     */
    public static final String EMP_NO = "empNo";
    public static final String EMP_NM = "empNm";
    public static final String DEPT_CD = "deptCd";
    public static final String ROLE_ID = "roleId";

}
