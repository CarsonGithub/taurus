package com.code.taurus.common.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 通用常量类
 *
 * @author 郑楷山
 **/

public interface CommonConstant {

	/**
	 * 包名配置
	 */
	String BASE_PACKAGE = "com.code.taurus";

	/**
	 * JSR 303 javax 包名
	 */
	String JAVAX_VALIDATION = "javax.validation";

	/**
	 * spring 的验证包名
	 */
	String SPRING_VALIDATION = "org.springframework.validation";

	/**
	 * spring web 下 参数绑定的包名
	 */
	String SPRING_WEB_BIND = "org.springframework.web.bind";
	String SPRING_HTTP_CONVERTER = "org.springframework.http.converter";

	/**
	 * 检索 和 排序 字段非法异常
	 */
	String SEARCH_NOT_ALLOW = "org.springframework.dao.InvalidDataAccessApiUsageException";
	String SORT_NOT_ALLOW = "org.springframework.data.mapping.PropertyReferenceException";

	/**
	 *  DAO下错误
	 */
	String DAO_OPERATE_EXCEPTION = "org.springframework.dao.DataIntegrityViolationException";

	/**
	 * 异常返回字段名
	 */
	String STATUS_CODE = "status";
	String ERROR_CODE = "error";
	String MESSAGE = "message";
	String TIMESTAMP = "timestamp";

	/**
	 *  上传路径
	 */
	String UPLOAD_FILE_PATH = "/upload-files";
	Set<String> SCRIPT_SUFFIX_SET = new HashSet<>(Arrays.asList("js", "css"));
	Set<String> IMAGE_SUFFIX_SET = new HashSet<>(Arrays.asList("bmp", "jpg", "png", "jpeg", "gif"));
	Set<String> VIDEO_SUFFIX_SET = new HashSet<>(Arrays.asList("mp4", "flv","avi","3gp","rm","rmvb","wmv","mov"));
	Set<String> DOC_SUFFIX_SET = new HashSet<>(Arrays.asList("doc", "docx", "pdf","txt","html"));

	Long MAX_IMAGE_SIZE = 5 * 1024 * 1024L;
	Long MAX_VIDEO_SIZE = 50 * 1024 * 1024L;
	Long MAX_DOC_SIZE = 5 * 1024 * 1024L;

	String USER_IMAGE_ICON = "/images/user/icon/" ;

	/**
	 *  异常类型
	 */
	String SYSTEM_EXCEPTION = "系统异常";
	String CUSTOM_EXCEPTION = "业务异常";

	/**
	 * token
	 */
	String TOKEN_HEADER = "Authorization";
	String TOKEN_PREFIX = "Bearer ";
	String TOKEN_REFRESH = "X-Refresh-Token";
	String TOKEN_SECRET = "thisIsjiSu_bang*app%%&secreT";

	/**
	 * 换行符
	 */
	String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * 请求头
	 */
	String X_REQUEST_ID = "X-Request-ID";
	String X_REAL_IP = "X-Real-IP";
	String X_FORWARDED_FOR = "X-Forwarded-For";

	/**
	 * 响应
	 **/
	String APPLICATION_UTF8 = "application/json;charset=utf-8";

	/**
	 * 日志输出格式
	 */
	String LOG_REQUEST_FORMAT = LINE_SEPARATOR + "@->> 请求日志({}):{}" ;
	String LOG_RESPONSE_FORMAT = LINE_SEPARATOR+"<<-@ 响应日志({}):{}";
	String LOG_EXCEPTION_BEFORE = LINE_SEPARATOR + "API日志拦截器before 出现异常.";
	String LOG_EXCEPTION_AFTER_RETURNING = LINE_SEPARATOR + "API日志拦截器after returning 出现异常.";
	String LOG_THROWING_AFTER_THROWING = LINE_SEPARATOR + "API日志拦截器after throwing 出现异常.";
	String LOG_JWT_AUTHENTICATION_SUCCESS = LINE_SEPARATOR + "JWT鉴权成功 :";

}
