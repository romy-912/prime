package com.romy.prime.common.token;

import com.romy.prime.organization.dvo.OrgUserDvo;
import com.romy.prime.organization.repository.OrgUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * packageName    : com.romy.prime.common.token
 * fileName       : JwtAuthService
 * author         : 김새롬이
 * date           : 2024-10-14
 * description    : UserDetailsService 구현체
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-14        김새롬이       최초 생성
 */
@Service
@RequiredArgsConstructor
public class JwtAuthService implements UserDetailsService {

    private final OrgUserRepository orgUserRepository;

    @Override
    public UserDetails loadUserByUsername(String empNo) throws UsernameNotFoundException {

        OrgUserDvo dvo = this.orgUserRepository.selectUserInfoByEmpNo(empNo);
        if (dvo == null) {
            throw new UsernameNotFoundException(empNo);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(dvo.getEmpNm()));
        grantedAuthorities.add(new SimpleGrantedAuthority(dvo.getRoleId()));
        grantedAuthorities.add(new SimpleGrantedAuthority(dvo.getDeptCd()));

        return new User(dvo.getEmpNo(), dvo.getPassword(), grantedAuthorities);
    }
}
