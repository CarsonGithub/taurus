package com.taurus.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 检索类型
 *
 * @author 郑楷山
 **/

@Getter
@AllArgsConstructor
public enum ConditionEnum{

    EQUAL("= value"),

    UNEQUAL("!= value"),

    IN("in (value)"),

    NOT_IN("not in (value)"),

    LIKE_BOTH("% value %"),

    LIKE_LEFT("% value"),

    LIKE_RIGHT("value %"),

    GT("> value"),

    GT_EQUAL(">= value"),

    LT("< value"),

    LT_EQUAL("<= value"),

    BETWEEN_EQUAL("LEFT <= X <= RIGHT"),

    NULL("is null"),

    NOT_NULL("is not null"),

    ;

    private final String type;

}
