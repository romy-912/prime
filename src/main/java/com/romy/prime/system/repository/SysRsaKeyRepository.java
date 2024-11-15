package com.romy.prime.system.repository;

import com.romy.prime.system.dvo.SysRsaKeyDvo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * packageName    : com.romy.prime.system.repository
 * fileName       : SysRsaKeyRepository
 * author         : 김새롬이
 * date           : 2024-11-13
 * description    : 암호화 키 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-13        김새롬이       최초 생성
 */
@Mapper
public interface SysRsaKeyRepository {

    void insertRsaKey(SysRsaKeyDvo dvo);

    String selectPrivateKey(@Param("sessionKey") String sessionKey);

    void deleteRsaKey(@Param("sessionKey") String sessionKey);
}
