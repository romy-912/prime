package com.romy.prime.organization.repository;

import com.romy.prime.organization.dvo.OrgUserDvo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import static com.romy.prime.organization.dto.OrgUserMgtDto.SearchReq;
import static com.romy.prime.organization.dto.OrgUserMgtDto.SearchRes;

/**
 * packageName    : com.romy.prime.organization.repository
 * fileName       : orgUserRepository
 * author         : 김새롬이
 * date           : 2024-10-14
 * description    : 사용자 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-14        김새롬이       최초 생성
 */
@Mapper
public interface OrgUserRepository {

    OrgUserDvo selectUserInfoByEmpNo(@Param("empNo") String empNo);

    void updatePasswordInvalid(OrgUserDvo dvo);

    void updateUserPassword(OrgUserDvo dvo);

    List<SearchRes> selectUserList(SearchReq dto);

    int selectEmpNoDupCheck(@Param("empNo") String empNo);

    int insertUser(OrgUserDvo dvo);

    int updateUser(OrgUserDvo dvo);

    int deleteUser(@Param("empNo") String empNo);

    int updatePasswordInit(OrgUserDvo dvo);

    int selectChildUserCheck(@Param("roleId") String roleId);

}
