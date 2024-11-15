package com.romy.prime.common.utils;

import com.romy.prime.common.Log;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.Map;

/**
 * packageName    : com.romy.prime.common.utils
 * fileName       : ParamUtils
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : Parameter Util (Log 용)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
public class ParamUtils {

    public static synchronized void viewParam(HttpServletRequest request) {
        if (request.getParameterMap().isEmpty()) {
            Log.Debug("─────────────────── Request Parameter Size Zero ────────────────────────");
            return;
        }

        Log.Debug("┌────────────────── Request Parameter View Start ────────────────────────");
        String paramName;
        String[] exCols;
        Map<?, ?> paramMap = request.getParameterMap();
        Enumeration<?> enumeration = request.getParameterNames();

        for (int i = 0; i < paramMap.size(); i++) {
            paramName = (String) enumeration.nextElement();
            exCols = (String[]) paramMap.get(paramName);
            if (exCols.length > 1) {
                for (String exCol : exCols) {
                    Log.Debug("│  {" + paramName + " = " + exCol + "}");
                }
            } else {
                Log.Debug("│  {" + paramName + " = " + exCols[0] + "}");
            }
        }
        Log.Debug("└────────────────── Request Parameter View End ──────────────────────────");
    }

}
