package com.romy.prime.system.dvo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.romy.prime.system.dvo
 * fileName       : SysRsaKeyDvo
 * author         : 김새롬이
 * date           : 2024-11-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-13        김새롬이       최초 생성
 */
@Getter
@Setter
public class SysRsaKeyDvo {
    
    // 세션키
    private String sessionKey;
    // 개인키
    private String privateKey;
    // 생성일시
    private LocalDateTime crtDthr;
}
