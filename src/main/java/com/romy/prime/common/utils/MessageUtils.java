package com.romy.prime.common.utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * packageName    : com.romy.prime.common.utils
 * fileName       : MessageUtils
 * author         : 김새롬이
 * date           : 2024-10-07
 * description    : 메시지 유틸
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-07        김새롬이       최초 생성
 */
@Component
public class MessageUtils {

    @Resource
    private MessageSource source;

    static MessageSource messageSource;

    @PostConstruct
    public void initialize() {
        messageSource = this.source;
    }

    /**
     * 메시지 조회
     *
     * @param msgCd 메시지 코드
     * @return 메시지
     */
    public static String getMessage(String msgCd) {
        if (StringUtils.isBlank(msgCd)) return "";

        try {
            return messageSource.getMessage(msgCd, null, Locale.KOREAN);
        } catch (NoSuchMessageException e) {
            return msgCd;
        }
    }

    /**
     * 메시지 조회
     *
     * @param msgCd 메시지 코드
     * @param args  파라미터
     * @return 메시지
     */
    public static String getMessage(String msgCd, Object[] args) {
        if (StringUtils.isBlank(msgCd)) {
            return "";
        } else if (args == null || args.length == 0) {
            return getMessage(msgCd);
        }

        try {
            return messageSource.getMessage(msgCd, args, Locale.KOREAN);
        } catch (NoSuchMessageException e) {
            return msgCd;
        }
    }

}
