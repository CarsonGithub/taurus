package com.code.taurus.common.util;

import com.code.taurus.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.id.UUIDGenerator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

/**
 * 帮助类: 请求参数获取
 *
 * @author 郑楷山
 **/

public class RequestUtil {

    public static String getRealIp() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return StringUtils.isBlank(request.getHeader(CommonConstant.X_FORWARDED_FOR))
                ?
                StringUtils.isBlank(request.getHeader(CommonConstant.X_REAL_IP))
                        ?
                        request.getRemoteAddr().trim()
                        :
                        request.getHeader(CommonConstant.X_REAL_IP).trim()
                :
                request.getHeader(CommonConstant.X_FORWARDED_FOR).split(",")[0].trim();
    }

    public static String getUri() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request.getRequestURI();
    }

    public static String buildRequestID(){
        return getUUID_8();
    }

    private final static String[] CHARS = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    private static String getUUID_8() {
        StringBuilder sb = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            sb.append(CHARS[x % 0x3E]);
        }
        return sb.toString();
    }

}
