package com.romy.prime.common.interceptor;

import com.romy.prime.common.Log;
import com.romy.prime.common.utils.ParamUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * packageName    : com.romy.prime.common.interceptor
 * fileName       : Interceptor
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : interceptor
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
@Component
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {

        String uri = request.getRequestURI();

        Log.Debug("[REQUEST] relativeUrl:" + uri);
        Log.Debug("[REQUEST] RemoteHost:" + request.getRemoteHost() + ":" + request.getRemotePort());

        // requestParam View
        ParamUtils.viewParam(request);

        // 처리 시간 계산
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        // log it
        Log.Debug("[" + handler + "] executeTime : " + executeTime + "ms");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Log.Debug("■■■■■■■■■■■■■■■■■■■■■■■ End ■■■■■■■■■■■■■■■■■■■■■■■");
    }
}
