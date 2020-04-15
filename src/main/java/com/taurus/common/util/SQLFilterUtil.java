package com.taurus.common.util;

import com.taurus.common.enums.ExceptionEnum;
import com.taurus.common.model.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * Description:sql敏感过滤
 *
 * @author 郑楷山
 **/
public class SQLFilterUtil {

    /**
     * SQL注入过滤
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        // 非法字符
        String[] keywords = { "master", "truncate", "insert", "select",
                "delete", "update", "declare", "alert", "create", "drop" };

        for (String keyword : keywords) {
            if (str.equalsIgnoreCase(keyword)) {
                throw new BusinessException(ExceptionEnum.SQL_SENSITIVE_CHARACTER);
            }
        }

        return str;
    }
}
