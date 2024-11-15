package com.romy.prime.common.token;

import com.romy.prime.common.Log;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * packageName    : com.romy.prime.common.token
 * fileName       : JwtRequestFilter
 * author         : 김새롬이
 * date           : 2024-10-14
 * description    : Reuqest Filter
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-14        김새롬이       최초 생성
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtAuthService jwtAuthService;

    private static final List<String> EXCLUDE_URL = List.of("/swagger-ui", "/v3/api-docs",
            "/auth/login", "auth/key", "/auth/user/password");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String reqAuth = request.getHeader("Authorization");

        String token;
        if (reqAuth != null && reqAuth.startsWith("Bearer")) {
            token = reqAuth.substring(7);
        } else {
            token = request.getHeader("accessToken");
        }

        try {
            String empNo = this.jwtProvider.extEmpNo(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (StringUtils.isNotBlank(empNo) && authentication == null) {
                UserDetails userDetails = this.jwtAuthService.loadUserByUsername(empNo);
                boolean isValid = this.jwtProvider.isValidToken(token, userDetails);

                if (isValid) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());

                    authToken.setDetails(token);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } // valid if
            } // empNo if
        } catch (Exception e) {
            Log.Error(e.getMessage());
        }
        
        response.setHeader("token", token);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return EXCLUDE_URL.stream().anyMatch(exclude -> (exclude.equalsIgnoreCase(path))
                || path.startsWith(exclude));
    }

}
