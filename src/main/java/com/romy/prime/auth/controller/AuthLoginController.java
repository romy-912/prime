package com.romy.prime.auth.controller;

import com.romy.prime.auth.service.AuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.romy.prime.auth.dto.AuthLoginDto.*;

/**
 * packageName    : com.romy.prime.auth.controller
 * fileName       : AuthLoginController
 * author         : 김새롬이
 * date           : 2024-10-17
 * description    : 로그인 관련 Controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-17        김새롬이       최초 생성
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "로그인")
@RequestMapping("/auth")
public class AuthLoginController {

    private final AuthLoginService authLoginService;


    @PostMapping("/key")
    @Operation(summary = "암호화 키 발급")
    public KeyRes createEncryptionKey() {
        return this.authLoginService.createEncryptionKey();
    }

    @GetMapping("/login")
    @Operation(summary = "로그인")
    public LoginRes execAuthLogin(@Valid LoginReq dto) throws Exception {
        return this.authLoginService.execAuthLogin(dto);
    }

    @PutMapping("/user/password")
    @Operation(summary = "사용자 비밀번호 변경")
    public LoginRes updateUserPassword(@RequestBody @Valid ChgPwReq dto) throws Exception {
        return this.authLoginService.updateUserPassword(dto);
    }



}
