package com.romy.prime.organization.converter;

import static com.romy.prime.organization.dto.OrgUserMgtDto.*;
import com.romy.prime.organization.dvo.OrgUserDvo;
import org.mapstruct.Mapper;

/**
 * packageName    : com.romy.prime.organization.converter
 * fileName       : OrgUserMgtConverter
 * author         : 김새롬이
 * date           : 2024-10-24
 * description    : 사용자 관리 converter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-24        김새롬이       최초 생성
 */
@Mapper(componentModel = "spring")
public interface OrgUserMgtConverter {

    OrgUserDvo saveReqToOrgUserDvo(SaveReq dto);

}
