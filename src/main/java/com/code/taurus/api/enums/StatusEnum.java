package com.code.taurus.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 数据状态（用于假删除，暂不用）
 *
 * @author 郑楷山
 **/

@Getter
@AllArgsConstructor
public enum StatusEnum {

	DISABLE(0),

	ENABLE(1),

	;

	private final int value;

}
