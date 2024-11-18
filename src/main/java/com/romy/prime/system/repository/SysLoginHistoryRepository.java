package com.romy.prime.system.repository;

import com.romy.prime.system.dvo.SysLoginHistoryDvo;
import org.apache.ibatis.annotations.Mapper;

/**
 * packageName    : com.romy.prime.system.repository
 * fileName       : SysLoginHistoryRepository
 * author         : 김새롬이
 * date           : 2024-11-18
 * description    : 로그인 이력 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-18        김새롬이       최초 생성
 */
@Mapper
public interface SysLoginHistoryRepository {

    void insertLoginHistory(SysLoginHistoryDvo dvo);
}
