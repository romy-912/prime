package com.romy.prime.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * packageName    : com.romy.prime.common
 * fileName       : Log
 * author         : 김새롬이
 * date           : 2024-10-18
 * description    : Logger
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-18        김새롬이       최초 생성
 */
public class Log {

    private static final Logger log = LoggerFactory.getLogger(Log.class);

    public static void Debug() {
        Debug("####################" + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    public static void DebugStart() {
        Log.Debug("####################" + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName() + " Start");
    }

    public static void DebugEnd() {
        Log.Debug("####################" + Thread.currentThread().getStackTrace()[2].getClassName() + "."
                + Thread.currentThread().getStackTrace()[2].getMethodName() + " End");
    }

    public static void Debug(String message) {
        if (log.isDebugEnabled())
            log.debug(message);
    }

    /**
     * 로그 정보
     *
     * @param message 메시지
     */
    public static void Info(String message) {
        if (log.isDebugEnabled())
            log.info(message);
    }

    /**
     * 로그 위험 정보
     *
     * @param message 메시지
     */
    public static void Warn(String message) {
        log.warn(message);
    }

    /**
     * 로그 에러 정보
     *
     * @param message 메시지
     */
    public static void Error(String message) {
        log.error(message);
    }

}
