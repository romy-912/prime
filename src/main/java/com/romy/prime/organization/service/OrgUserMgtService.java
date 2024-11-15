package com.romy.prime.organization.service;

import static com.romy.prime.organization.dto.OrgUserMgtDto.*;

import com.romy.prime.common.PrimeConstant;
import com.romy.prime.common.encrypt.PRIME_CRYPT_HASH;
import com.romy.prime.common.utils.PrimeUtils;
import com.romy.prime.organization.converter.OrgUserMgtConverter;
import com.romy.prime.organization.dvo.OrgUserDvo;
import com.romy.prime.organization.repository.OrgUserRepository;
import com.romy.prime.system.dvo.SysCryptDvo;
import com.romy.prime.system.repository.SysConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * packageName    : com.romy.prime.organization.service
 * fileName       : OrgUserMgtService
 * author         : 김새롬이
 * date           : 2024-10-22
 * description    : 사용자 관리 Service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-22        김새롬이       최초 생성
 */
@Service
@RequiredArgsConstructor
public class OrgUserMgtService {

    private final OrgUserMgtConverter converter;
    private final OrgUserRepository orgUserRepository;
    private final SysConfigRepository sysConfigRepository;

    /**
     * 사용자 관리 조회
     * @param dto 조회 파라미터
     * @return 데이터 리스트
     */
    public List<SearchRes> getUserMgtList(SearchReq dto) {
        return this.orgUserRepository.selectUserList(dto);
    }

    /**
     * 사용자 관리 저장
     * @param dtos 데이터 리스트
     * @return 처리 건수
     */
    @Transactional
    public int saveUserInfo(List<SaveReq> dtos) throws NoSuchAlgorithmException {
        int count = 0;

        // 암호화 정보 조회
        SysCryptDvo cryptDvo = this.sysConfigRepository.selectCryptInfo();

        for (SaveReq dto : dtos) {
            // row 상태
            String rowStatus = dto.rowStatus();

            OrgUserDvo dvo = this.converter.saveReqToOrgUserDvo(dto);

            // 신규
            if (PrimeConstant.ROW_INSERT.equals(rowStatus)) {
                // 사원번호
                String empNo = dvo.getEmpNo();

                // 사원번호 중복 체크
                int dupCnt = this.orgUserRepository.selectEmpNoDupCheck(empNo);
                PrimeUtils.checkDuplicationValue(dupCnt, PrimeConstant.EMP_NO, empNo);

                // 비밀번호 초기화여부 - 초기화 처리
                dvo.setPwInitYn(PrimeConstant.YN_Y);
                // 잠금여부해제
                dvo.setLockYn(PrimeConstant.YN_N);
                // 비밀번호 오류 횟수 - 초기화 처리
                dvo.setPwErrCnt((short) 0);
                // 비밀번호 암호화
                String hashPw = PRIME_CRYPT_HASH.getHash(empNo + "!!", cryptDvo.getSalt(),
                        cryptDvo.getInterNum());
                dvo.setPassword(hashPw);

                count += this.orgUserRepository.insertUser(dvo);
                
            // 수정
            } else if(PrimeConstant.ROW_UPDATE.equals(rowStatus)) {
                count += this.orgUserRepository.updateUser(dvo);
            }
        }

        return count;
    }

    /**
     * 사용자 삭제
     * @param empNos 사원번호 리스트
     * @return 처리 건수
     */
    @Transactional
    public int removeUserInfo(List<String> empNos) {

        int count = 0;

        for (String empNo : empNos) {
            count += this.orgUserRepository.deleteUser(empNo);
        }

        return count;
    }


    /**
     * 비밀번호 초기화
     * @param empNos 사번 리스트
     * @return 처리 건수
     */
    @Transactional
    public int updatePasswordInit(List<String> empNos) throws NoSuchAlgorithmException {
        int count = 0;

        // 암호화 정보 조회
        SysCryptDvo cryptDvo = this.sysConfigRepository.selectCryptInfo();

        OrgUserDvo dvo = new OrgUserDvo();
        for (String empNo : empNos) {
            dvo.setEmpNo(empNo);
            // 비밀번호 초기화여부 - 초기화 처리
            dvo.setPwInitYn(PrimeConstant.YN_Y);
            // 잠금여부해제
            dvo.setLockYn(PrimeConstant.YN_N);
            // 비밀번호 오류 횟수 - 초기화 처리
            dvo.setPwErrCnt((short) 0);
            // 비밀번호 암호화
            String hashPw = PRIME_CRYPT_HASH.getHash(empNo + "!!", cryptDvo.getSalt(),
                    cryptDvo.getInterNum());
            dvo.setPassword(hashPw);

            count += this.orgUserRepository.updatePasswordInit(dvo);
        }

        return count;
    }


}
