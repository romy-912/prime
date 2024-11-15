package com.romy.prime.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * packageName    : com.romy.prime.auth.dto
 * fileName       : AuthLoginDto
 * author         : 김새롬이
 * date           : 2024-10-17
 * description    : 로그인 관련 dto
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-17        김새롬이       최초 생성
 */
public class AuthLoginDto {

    @Schema(name = "암호화 Res")
    public record KeyRes(
            @Schema(title = "세션키")
            String sessionKey,
            @Schema(title = "Modulus")
            String modulus,
            @Schema(title = "Exponent")
            String exponent
    ){}


    @Schema(name = "로그인 Req")
    public record LoginReq(
            @NotBlank
            @Schema(title = "아이디", description = "암호화 된 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
            String id,
            @NotBlank
            @Schema(title = "비밀번호", description = "암호화 된 비밀번호", requiredMode =
                    Schema.RequiredMode.REQUIRED)
            String password,
            @NotBlank
            @Schema(title = "세션키", requiredMode = Schema.RequiredMode.REQUIRED)
            String sessionKey
    ) {}

    @Schema(name = "로그인 Res")
    public record LoginRes(
            @Schema(title = "토큰")
            String token,
            @Schema(title = "비밀번호 초기화 여부", description = "초기화 : 1, 해제 : 0", type = "number",
                    allowableValues = {"0","1"})
            byte pwInitYn
    ) {}

    @Schema(name = "비밀번호 재설정 Req")
    public record ChgPwReq(
            @NotBlank
            @Schema(title = "아이디", description = "암호화 된 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
            String id,
            @NotBlank
            @Schema(title = "비밀번호", description = "암호화 된 비밀번호", requiredMode =
                    Schema.RequiredMode.REQUIRED)
            String password,
            @NotBlank
            @Schema(title = "세션키", requiredMode = Schema.RequiredMode.REQUIRED)
            String sessionKey
    ){}

}
