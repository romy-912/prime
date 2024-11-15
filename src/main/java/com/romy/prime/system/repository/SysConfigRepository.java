package com.romy.prime.system.repository;

import com.romy.prime.system.dvo.SysCryptDvo;
import org.apache.ibatis.annotations.Mapper;

/**
 * packageName    : com.romy.prime.system.repository
 * fileName       : SysConfigRepository
 * author         : 김새롬이
 * date           : 2024-10-22
 * description    : 설정 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-22        김새롬이       최초 생성
 */
@Mapper
public interface SysConfigRepository {

    SysCryptDvo selectCryptInfo();
}
