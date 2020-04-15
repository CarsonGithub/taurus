package com.taurus.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * api响应日志输出model
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
public class SysResponseLog {


	/**
	 * 处理花费时间
	 */
	private long processTime;

	/**
	 * 错误码
	 */
	private int code;

	/**
	 * 返回状态 与http状态一致
	 */
	private int status;

	/**
	 * 异常类名
	 */
	private String exceptionClassName;

	/**
	 * 返回值
	 */
	private Object resultValue;

	/**
	 * 是否忽略保存参数和返回值
	 */
	private Boolean isIgnoreUri;


}
