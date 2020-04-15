package com.taurus.common.model;

import com.taurus.common.enums.ExceptionEnum;

/**
 * 无数据异常
 *
 * @author 郑楷山
 **/

public class NoRecordException extends BusinessException {

	public NoRecordException(){
		super(ExceptionEnum.NO_EXIST_ERROR);
	}

}
