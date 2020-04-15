package com.taurus.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 业务异常类型
 *
 * @author 郑楷山
 **/

@AllArgsConstructor
@Getter
public enum  ExceptionEnum {

	SERVER_ERROR(500, 51, "系统繁忙!"),

	CLASS_NOT_FOUND_ERROR(500, 52, "初始化找不到类!"),

	STATIC_HTML_ERROR(500, 53, "生成静态页时出错!"),

	INIT_ENTITY_ERROR(500, 54, "设置实体初始值出错!"),

	CACHE_ERROR(500, 55, "缓存过程出错!"),

	NO_FIELD_ERROR(500, 56, "找不到对应的字段!"),

	REDIS_CONNECT_FIELD_ERROR(500, 57, "Redis连接失败!"),

	SMS_OPERATE_ERROR(500, 58, "短信服务连接失败!"),

	DEMO_POST_ERROR(500, 59, "数据传输到Demo系统失败!"),

	NO_PERMISSION_ACCESS(403, 41, "No permission!"),

	TOKEN_ERROR(401, 42, "JWT auth  failed!"),

	PARAMETER_ERROR(400, 43, "Error params:%s"),

	SQL_SENSITIVE_CHARACTER(400,44,"包含SQL非法字符"),

	UPLOAD_SUFFIX_ERROR(400, 1, "上传文件后缀不正确!"),

	UPLOAD_SIZE_LIMIT(400, 2, "上传文件超过限制大小%d"),

	USER_PASSWORD_ERROR(400, 3, "账号/密码有误!"),

	ADD_UPDATE_DELETE_ERROR(400, 4, "增/删/改类型操作未成功!"),

	DAO_OPERATE_ERROR(400, 5, "数据操作出错(如名称重复,分类ID不存在,数据受约束无法删除)!"),

	NO_EXIST_ERROR(400, 6, "找不到对应数据!"),

	UPLOAD_NULL_ERROR(400, 7, "上传文件为空!"),

	UPLOAD_FILE_NAME_NULL_ERROR(400, 8, "上传文件名为空!"),

	DELETE_USING_ERROR(400, 9, "使用中的数据不能被删除!"),

	FILE_FOUND_ERROR(400, 10, "文件路径非法,或者文件不存在!"),

	DELETE_CATE_USING_ERROR(400,11,"该分类在使用中，或者该分类下有数据不能删除!"),

	CLEAR_CACHE_ERROR(400,12,"无数据变更,无需清空缓存!"),

	EXPORT_ERROR(400,12,"导出失败,IO过程出现问题!"),

	SEO_DELETE_REFUSE_ERROR(400,12,"SEO资讯不可删除!"),

	SMS_REPEAT_ERROR(400, 13, "同个手机号短时间不能重复请求!"),

	SMS_CHECK_ERROR(400,14,"短信验证码有误!"),

	PHONE_NUMBER_NULL_ERROR(400,15,"手机号码不能为空!"),

	;

	private final int status;

	private final int error;

	private final String description;


}
