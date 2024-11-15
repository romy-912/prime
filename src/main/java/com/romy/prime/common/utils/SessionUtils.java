package com.romy.prime.common.utils;

import com.romy.prime.common.token.JwtProvider;
import com.romy.prime.organization.dvo.OrgUserDvo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.romy.prime.common.utils
 * fileName       : SessionUtils
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : 세션 관련 Util
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Component
public class SessionUtils {

    @Resource
    private JwtProvider provider;

    static JwtProvider jwtProvider;

    @PostConstruct
    public void initialize() {
        jwtProvider = this.provider;
    }

    private static String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getDetails() instanceof String)) return "";

        return (String) authentication.getDetails();
    }

    /**
     * 사원번호 가져오기
     */
    public static String getEmpNo() {
        String token = getToken();
        return jwtProvider.extEmpNo(token);
    }

    /**
     * 사용자 정보 가져오기
     */
    public static OrgUserDvo getUserInfo() {
        String token = getToken();
        if (StringUtils.isBlank(token)) return null;

        // 사원번호
        String empNo = jwtProvider.extEmpNo(token);
        // 성명
        String empNm = jwtProvider.extEmpNm(token);
        // 권한 ID
        String roleId = jwtProvider.extRoleId(token);
        // 부서코드
        String deptCd = jwtProvider.extDeptCd(token);

        OrgUserDvo dvo = new OrgUserDvo();
        dvo.setEmpNo(empNo);
        dvo.setEmpNm(empNm);
        dvo.setRoleId(roleId);
        dvo.setDeptCd(deptCd);

        return dvo;
    }
}
