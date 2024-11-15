package com.romy.prime.organization.controller;

import com.romy.prime.common.response.SaveProcessResponse;

import static com.romy.prime.organization.dto.OrgUserMgtDto.*;
import com.romy.prime.organization.service.OrgUserMgtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * packageName    : com.romy.prime.organization.controller
 * fileName       : OrgUserMgtController
 * author         : 김새롬이
 * date           : 2024-10-22
 * description    : 사용자 관리 Controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-22        김새롬이       최초 생성
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 관리")
@RequestMapping("/organization/user")
public class OrgUserMgtController {

    private final OrgUserMgtService orgUserMgtService;

    @GetMapping
    @Operation(summary = "사용자 관리 조회")
    public List<SearchRes> getUserMgtList(SearchReq dto) {
        return this.orgUserMgtService.getUserMgtList(dto);
    }

    @PostMapping
    @Operation(summary = "사용자 관리 저장")
    public SaveProcessResponse saveUserInfo(@RequestBody @NotEmpty @Valid List<SaveReq> dtos) throws NoSuchAlgorithmException {
        return new SaveProcessResponse(this.orgUserMgtService.saveUserInfo(dtos), "");
    }

    @DeleteMapping
    @Operation(summary = "사용자 삭제")
    public SaveProcessResponse removeUserInfo(@RequestBody @NotEmpty List<String> empNos) {
        return new SaveProcessResponse(this.orgUserMgtService.removeUserInfo(empNos), "");
    }

    @PutMapping("/password/init")
    @Operation(summary = "비밀번호 초기화")
    public SaveProcessResponse updatePasswordInit(@RequestBody @NotEmpty List<String> empNos) throws NoSuchAlgorithmException {
        return new SaveProcessResponse(this.orgUserMgtService.updatePasswordInit(empNos), "");
    }


}
