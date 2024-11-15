package com.romy.prime.auth.service;

import static com.romy.prime.auth.dto.AuthLoginDto.*;

import com.romy.prime.common.PrimeConstant;
import com.romy.prime.common.encrypt.PRIME_CRYPT_HASH;
import com.romy.prime.common.encrypt.RSA;
import com.romy.prime.common.exception.PrimeException;
import com.romy.prime.common.exception.UnAuthorizationException;
import com.romy.prime.common.token.JwtProvider;
import com.romy.prime.common.utils.PrimeUtils;
import com.romy.prime.organization.dvo.OrgUserDvo;
import com.romy.prime.organization.repository.OrgUserRepository;
import com.romy.prime.system.dvo.SysCryptDvo;
import com.romy.prime.system.dvo.SysRsaKeyDvo;
import com.romy.prime.system.repository.SysConfigRepository;
import com.romy.prime.system.repository.SysRsaKeyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * packageName    : com.romy.prime.auth.service
 * fileName       : AuthLoginService
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : 로그인 관련 서비스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final JwtProvider jwtProvider;

    private final OrgUserRepository orgUserRepository;
    private final SysRsaKeyRepository sysRsaKeyRepository;
    private final SysConfigRepository sysConfigRepository;

    /**
     * 암호화 키 발급
     * @return 암호화 키 정보
     */
    @Transactional
    public KeyRes createEncryptionKey() {

        RSA rsa = RSA.getEncKey();

        String sessionKey = RandomStringUtils.randomAlphanumeric(20);

        SysRsaKeyDvo dvo = new SysRsaKeyDvo();
        dvo.setSessionKey(sessionKey);
        dvo.setPrivateKey(rsa.getPrivateKey());
        dvo.setCrtDthr(LocalDateTime.now());

        sysRsaKeyRepository.insertRsaKey(dvo);

        return new KeyRes(sessionKey, rsa.getPublicKeyModulus(), rsa.getPublicKeyExponent());
    }


    /**
     * 로그인 처리
     *
     * @param dto 로그인 파라미터
     * @return 토큰 데이터
     */
    public LoginRes execAuthLogin(LoginReq dto) throws Exception {
        // 암호화 사원번호
        String encEmpNo = dto.id();
        // 암호화 비밀번호
        String encPw = dto.password();
        // 세션키
        String sessionKey = dto.sessionKey();

        // 개인키 조회
        String privateKey = this.sysRsaKeyRepository.selectPrivateKey(sessionKey);
        if (StringUtils.isBlank(privateKey)) {
            // 잘못된 접근입니다.
            throw new PrimeException(PrimeConstant.AUTH_INVALID_ACCESS);
        }

        // 복호화 처리
        String empNo = RSA.decryptRsa(privateKey, encEmpNo);
        String passwd = RSA.decryptRsa(privateKey, encPw);

        OrgUserDvo dvo = this.orgUserRepository.selectUserInfoByEmpNo(empNo);
        // dvo 널 체크
        if (dvo == null) {
            // 인증 오류, 사용자가 없습니다. [아이디: {0}]
            PrimeUtils.unAuthorized(empNo);
        }

        // 잠금여부
        byte lockYn = dvo.getLockYn();

        if (lockYn == PrimeConstant.YN_Y) {
            // 계정이 잠겼습니다. 관리자에게 문의해 주세요.
            throw new UnAuthorizationException(PrimeConstant.AUTH_USER_LOCK);
        }

        // 비밀번호 초기화여부
        byte initYn = dvo.getPwInitYn();

        // 암호화 정보 조회
        SysCryptDvo cryptDvo = this.sysConfigRepository.selectCryptInfo();
        // 암호화 처리된 패스워드
        String hashPw = PRIME_CRYPT_HASH.getHash(passwd, cryptDvo.getSalt(), cryptDvo.getInterNum());
        // 비밀번호가 일치하지 않을경우
        if (!dvo.getPassword().equals(hashPw)) {
            // 비밀번호 오류 횟수 증가, 오류 메시지 발생으로 인해 Transactional 제외 처리
            this.orgUserRepository.updatePasswordInvalid(dvo);

            // 비밀번호가 일치하지 않습니다.
            throw new UnAuthorizationException(PrimeConstant.AUTH_INVALID_PASSWD);
        }

        // 개인키 삭제
        this.sysRsaKeyRepository.deleteRsaKey(sessionKey);
    
        // 비밀번호 초기화인 경우 비밀번호 변경 처리 해야함
        String token = initYn == PrimeConstant.YN_Y ? "" : this.jwtProvider.createToken(dvo);
        return new LoginRes(token, initYn);
    }

    /**
     * 사용자 비밀번호 변경
     * @param dto 비밀번호 변경 파라미터
     * @return 토큰
     */
    @Transactional
    public LoginRes updateUserPassword(ChgPwReq dto) throws Exception {
        // 암호화 사원번호
        String encEmpNo = dto.id();
        // 암호화 비밀번호
        String encPw = dto.password();
        // 세션키
        String sessionKey = dto.sessionKey();

        // 개인키 조회
        String privateKey = this.sysRsaKeyRepository.selectPrivateKey(sessionKey);
        if (StringUtils.isBlank(privateKey)) {
            // 잘못된 접근입니다.
            throw new PrimeException(PrimeConstant.AUTH_INVALID_ACCESS);
        }

        // 개인키 삭제
        this.sysRsaKeyRepository.deleteRsaKey(sessionKey);

        // 복호화 처리
        String empNo = RSA.decryptRsa(privateKey, encEmpNo);
        String passwd = RSA.decryptRsa(privateKey, encPw);

        // 암호화 정보 조회
        SysCryptDvo cryptDvo = this.sysConfigRepository.selectCryptInfo();
        // 암호화 처리된 패스워드
        String hashPw = PRIME_CRYPT_HASH.getHash(passwd, cryptDvo.getSalt(), cryptDvo.getInterNum());

        OrgUserDvo dvo = new OrgUserDvo();
        dvo.setEmpNo(empNo);
        dvo.setPassword(hashPw);
        // 비밀번호 초기화 여부
        dvo.setPwInitYn(PrimeConstant.YN_N);

        this.orgUserRepository.updateUserPassword(dvo);

        dvo = this.orgUserRepository.selectUserInfoByEmpNo(empNo);

        return new LoginRes(this.jwtProvider.createToken(dvo), PrimeConstant.YN_N);
    }

}
