package com.romy.prime.common.token;

import com.romy.prime.common.Log;
import com.romy.prime.common.PrimeConstant;
import com.romy.prime.common.encrypt.RSA;
import com.romy.prime.organization.dvo.OrgUserDvo;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

/**
 * packageName    : com.romy.prime.common.token
 * fileName       : JwtComponent
 * author         : 김새롬이
 * date           : 2024-10-14
 * description    : Token 관련 Component
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-14        김새롬이       최초 생성
 */
@Component
public class JwtProvider {

    private PrivateKey secretKey;
    private PublicKey publicKey;

    /**
     * 개인키, 공개키 셋팅
     */
    @PostConstruct
    protected void init() throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        String privateKeyPem = Files.readString(Paths.get("src/main/resources/token/private.pem"), StandardCharsets.UTF_8);
        String publicKeyPem = Files.readString(Paths.get("src/main/resources/token/public.pem"), StandardCharsets.UTF_8);

        this.secretKey = RSA.pemToPrivateKey(privateKeyPem);
        this.publicKey = RSA.pemToPublicKey(publicKeyPem);
    }

    /**
     * 토큰 생성
     *
     * @param dvo 사용자 dvo
     * @return 토큰 데이터
     */
    public String createToken(OrgUserDvo dvo) {
        LocalDateTime now = LocalDateTime.now();

        Claims claims = Jwts.claims().add(PrimeConstant.EMP_NM, dvo.getEmpNm()).add(PrimeConstant.ROLE_ID, dvo.getRoleId())
                .add(PrimeConstant.DEPT_CD, dvo.getDeptCd()).id(dvo.getEmpNo()).issuer(dvo.getEmpNm())
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(now.plusDays(1L).atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        return Jwts.builder().claims(claims).signWith(this.secretKey).compact();
    }

    /**
     * 만료일자 추출
     *
     * @param token 토큰
     * @return 만료일자
     */
    private Date extExpireDate(String token) {
        return this.extClaim(token, Claims::getExpiration);
    }

    /**
     * 사원번호 추출
     *
     * @param token 토큰
     * @return 사원번호
     */
    public String extEmpNo(String token) {
        if (StringUtils.isBlank(token) || "null".equals(token)) return "";

        return this.extClaim(token, Claims::getId);
    }

    /**
     * 성명 추출
     *
     * @param token 토큰
     * @return 성명
     */
    public String extEmpNm(String token) {
        if (StringUtils.isBlank(token) || "null".equals(token)) return "";

        return this.extClaim(token, Claims::getIssuer);
    }

    /**
     * 권한 ID 추출
     *
     * @param token 토큰
     * @return 권한 ID
     */
    public String extRoleId(String token) {
        if (StringUtils.isBlank(token) || "null".equals(token)) return "";

        Claims claims = this.extAllClaims(token);

        return (String) claims.get(PrimeConstant.ROLE_ID);
    }

    /**
     * 부서코드 추출
     *
     * @param token 토큰
     * @return 부서코드
     */
    public String extDeptCd(String token) {
        if (StringUtils.isBlank(token) || "null".equals(token)) return "";

        Claims claims = this.extAllClaims(token);

        return (String) claims.get(PrimeConstant.DEPT_CD);
    }

    /**
     * 토큰 정보 조회
     *
     * @param token          토큰
     * @param claimsResolver Claims Function
     * @return 토큰 정보
     */
    private <T> T extClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = this.extAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * 토큰 파싱
     *
     * @param token 토큰
     * @return 토큰 Payload
     */
    private Claims extAllClaims(String token) {

        Claims body = null;

        try {
            body =
                    Jwts.parser().verifyWith(this.publicKey).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            Log.Debug(e.getMessage());
            throw e;

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            Log.Debug(e.getMessage());
        }
        return body;
    }

    /**
     * 토큰 만료 체크
     *
     * @param token 토큰
     * @return 만료여부
     */
    public boolean isTokenExpired(String token) {
        if (StringUtils.isBlank(token) || "null".equals(token)) return true;

        try {
            Date date = this.extExpireDate(token);
            return date.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 토큰 유효성 체크
     *
     * @param token       토큰
     * @param userDetails 유저정보
     * @return 유효성 여부
     */
    public boolean isValidToken(String token, UserDetails userDetails) {
        if (StringUtils.isBlank(token) || "null".equals(token) || userDetails == null) return false;

        final String empNo = this.extEmpNo(token);
        boolean isExpired = this.isTokenExpired(token);

        return userDetails.getUsername().equals(empNo) && !isExpired;
    }
}
