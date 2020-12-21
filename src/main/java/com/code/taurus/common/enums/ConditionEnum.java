package com.code.taurus.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 检索类型
 *
 * @author 郑楷山e
 **/

@Getter
@AllArgsConstructor
public enum ConditionEnum {

    EQUAL("EQUAL", "= value"),

    NOT_EQUAL("NOT_EQUAL", "!= value"),

    IN("IN", "in (value1,value2....)"),

    NOT_IN("NOT_IN", "not in (value1,value2....)"),

    EXISTS("EXISTS", "in (value1,value2....)"),

    NOT_EXISTS("NOT_EXISTS", "not in (value1,value2....)"),

    LIKE_BOTH("LIKE_BOTH", "% value %"),

    LIKE_LEFT("LIKE_LEFT", "% value"),

    LIKE_RIGHT("LIKE_RIGHT", "value %"),

    GT("GT", "> value"),

    GT_EQUAL("GT_EQUAL", ">= value"),

    LT("LT", "< value"),

    LT_EQUAL("LT_EQUAL", "<= value"),

    NULL("NULL", "is null"),

    NOT_NULL("NOT_NULL", "is not null"),

    MULTI_AND("MULTI_AND", "and conditions"),

    MULTI_OR("MULTI_OR", "or conditions"),

    @Deprecated
    BETWEEN_EQUAL("BETWEEN_EQUAL", "LEFT <= X <= RIGHT"),

    ;

    private final String value;
    private final String description;

}
