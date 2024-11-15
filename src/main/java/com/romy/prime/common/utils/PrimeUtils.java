package com.romy.prime.common.utils;

import com.romy.prime.common.PrimeConstant;
import com.romy.prime.common.exception.PrimeException;
import com.romy.prime.common.exception.UnAuthorizationException;
import com.romy.prime.common.exception.ValidationException;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

/**
 * packageName    : com.romy.prime.common.utils
 * fileName       : PrimeUtils
 * author         : 김새롬이
 * date           : 2024-10-21
 * description    : 시스템 공통 Utils
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-21        김새롬이       최초 생성
 */
public class PrimeUtils {

    /**
     * 필수 항목 체크
     *
     * @param value 데이터값
     * @param field 필드(컬럼)명
     */
    public static void checkRequiredValue(String value, @NotBlank String field) {
        if (StringUtils.isBlank(value)) {
            Object[] keys = new Object[]{1};
            keys[0] = MessageUtils.getMessage(field);

            String msg = MessageUtils.getMessage(PrimeConstant.COMMON_REQUIRED_VALUE, keys);
            throw new ValidationException(msg);
        }
    }

    /**
     * 양수 체크
     *
     * @param value 데이터값
     * @param field 필드(컬럼)명
     */
    public static void checkPositiveValue(int value, @NotBlank String field) {
        if (value <= 0) {
            Object[] keys = new Object[]{1};
            keys[0] = MessageUtils.getMessage(field);

            String msg = MessageUtils.getMessage(PrimeConstant.COMMON_POSITIVE_VALUE, keys);
            throw new ValidationException(msg);
        }
    }

    /**
     * 널 체크 (Null일 경우 에러 메시지)
     *
     * @param obj   데이터
     * @param msgCd 메시지 코드
     */
    public static void checkNotNullData(Object obj, String msgCd) {
        if (obj == null) {
            String msg = MessageUtils.getMessage(msgCd);
            throw new PrimeException(msg);
        }
    }

    /**
     * 널 체크 (Null일 경우 에러 메시지)
     *
     * @param obj   데이터
     * @param msgCd 메시지 코드
     * @param args  파라미터
     */
    public static void checkNotNullData(Object obj, String msgCd, Object[] args) {
        if (obj == null) {
            String msg = MessageUtils.getMessage(msgCd, args);
            throw new PrimeException(msg);
        }
    }

    /**
     * 결과 데이터 값 체크 (0일 경우 유효성 메시지)
     *
     * @param result 결과 데이터
     * @param field  필드
     * @param value  입력 값
     */
    public static void checkResultZeroValue(int result, String field, String value) {
        if (result == 0) {
            Object[] keys = new Object[]{MessageUtils.getMessage(field), value};
            throw new PrimeException(PrimeConstant.COMMON_INVALID_VALUE, keys);
        }
    }

    /**
     * 결과 데이터 값 체크 (0보다 클 경우 유효성 메시지)
     *
     * @param result 결과 데이터
     * @param field  필드
     * @param value  입력 값
     */
    public static void checkResultNotZeroValue(int result, String field, String value) {
        if (result > 0) {
            Object[] keys = new Object[]{MessageUtils.getMessage(field), value};
            throw new PrimeException(PrimeConstant.COMMON_INVALID_VALUE, keys);
        }
    }

    /**
     * 결과 데이터 중복 체크 (0보다 클 경우 에러 메시지)
     *
     * @param result 결과 데이터
     * @param field  필드
     * @param value  입력 값
     */
    public static void checkDuplicationValue(int result, String field, String value) {
        if (result > 0) {
            Object[] keys = new Object[]{MessageUtils.getMessage(field), value};
            // 입력한 {0}은(는) 이미 존재하는 데이터 입니다. [{1}]
            throw new PrimeException(PrimeConstant.COMMON_DUPLICATE_VALUE, keys);
        }
    }

    /**
     * 인증 오류
     *
     * @param empNo 사원번호
     */
    public static void unAuthorized(String empNo) {
        String msg = MessageUtils.getMessage(PrimeConstant.AUTH_USER_EMPTY, new Object[]{empNo});
        throw new UnAuthorizationException(msg);
    }

}
