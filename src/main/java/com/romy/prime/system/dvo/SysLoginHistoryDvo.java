package com.romy.prime.system.dvo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.romy.prime.system.dvo
 * fileName       : SysLoginHistoryDvo
 * author         : 김새롬이
 * date           : 2024-11-18
 * description    : 로그인 이력 dvo
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-18        김새롬이       최초 생성
 */

@Getter
@Setter
public class SysLoginHistoryDvo {

    private String empNo;

    private LocalDateTime loginDthr = LocalDateTime.now();

}
