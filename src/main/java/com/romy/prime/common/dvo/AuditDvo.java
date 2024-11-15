package com.romy.prime.common.dvo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.romy.prime.common.dvo
 * fileName       : AuditDvo
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : Audit dvo (시스템 감사)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Getter
@Setter
public class AuditDvo {

    // 생성일시
    private LocalDateTime crtDthr;
    // 생성자ID
    private String crtUserId;
    // 수정일시
    private LocalDateTime updDthr;
    // 수정자ID
    private String updUserId;

}
