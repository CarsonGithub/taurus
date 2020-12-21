package com.code.taurus.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 业务异常类型
 *
 * @author 郑楷山
 **/

@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    SERVER_ERROR(500, "系统繁忙!"),

    CLASS_NOT_FOUND_ERROR(500, "初始化找不到类!"),

    INIT_ENTITY_ERROR(500, "设置实体初始值出错!"),

    NO_FIELD_ERROR(500, "找不到对应的字段!"),

    REDIS_CONNECT_FIELD_ERROR(500, "Redis连接失败!"),

    SMS_OPERATE_ERROR(500, "短信服务连接失败!"),

    LIST_DATA_ERROR(500, "获取列表数据时取不到任何字段!"),

    NO_PERMISSION_ACCESS(403, "无路径访问权限!"),

    TOKEN_ERROR(401, "登录校验失败!"),

    PARAMETER_ERROR(400, "参数错误!"),

    SQL_SENSITIVE_CHARACTER(400, "包含SQL非法字符"),

    UPLOAD_SUFFIX_ERROR(400, "上传文件后缀不正确!"),

    UPLOAD_SIZE_LIMIT(400, "上传文件超过限制大小%d"),

    USER_PASSWORD_ERROR(400, "账号/密码有误!"),

    DAO_OPERATE_ERROR(400, "数据操作出错(如名称重复,分类ID不存在,数据受约束无法删除)!"),

    NO_EXIST_ERROR(400, "找不到对应数据!"),

    UPLOAD_NULL_ERROR(400, "上传文件为空!"),

    UPLOAD_FILE_NAME_NULL_ERROR(400, "上传文件名为空!"),

    DELETE_USING_ERROR(400, "使用中的数据不能被删除!"),

    FILE_FOUND_ERROR(400, "文件路径非法,或者文件不存在!"),

    DELETE_CATE_USING_ERROR(400, "该分类在使用中，或者该分类下有数据不能删除!"),

    EXPORT_ERROR(400, "导出失败,IO过程出现问题!"),

    SMS_REPEAT_ERROR(400, "同个手机号短时间不能重复请求!"),

    SMS_CHECK_ERROR(400, "短信验证码有误!"),

    PHONE_NUMBER_NULL_ERROR(400, "手机号码不能为空!"),

    ;

    private final int status;

    private final String message;


}
