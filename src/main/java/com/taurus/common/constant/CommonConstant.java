package com.taurus.common.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 通用常量类
 *
 * @author 郑楷山
 **/

public class CommonConstant {

	/**
	 * 包名配置
	 */
	public static final String BASE_PACKAGE = "com.taurus";
	public static final String BACKEND_CONTROLLER_PACKAGE = BASE_PACKAGE + ".api.controller";
	public static final String COMMON_CONTROLLER_PACKAGE = BASE_PACKAGE + ".common.controller";

	/**
	 * JSR 303 javax 包名
	 */
	public static final String JAVAX_VALIDATION = "javax.validation";

	/**
	 * spring 的验证包名
	 */
	public static final String SPRING_VALIDATION = "org.springframework.validation";

	/**
	 * spring web 下 参数绑定的包名
	 */
	public static final String SPRING_WEB_BIND = "org.springframework.web.bind";
	public static final String SPRING_HTTP_CONVERTER = "org.springframework.http.converter";

	/**
	 * 检索 和 排序 字段非法异常
	 */
	public static final String SEARCH_NOT_ALLOW = "org.springframework.dao.InvalidDataAccessApiUsageException";
	public static final String SORT_NOT_ALLOW = "org.springframework.data.mapping.PropertyReferenceException";

	/**
	 *  DAO下错误
	 */
	public static final String DAO_OPERATE_EXCEPTION = "org.springframework.dao.DataIntegrityViolationException";

	/**
	 * 异常返回字段名
	 */
	public static final String STATUS_CODE = "status";
	public static final String ERROR_CODE = "error";
	public static final String MESSAGE = "message";
	public static final String TIMESTAMP = "timestamp";

	/**
	 *  上传路径
	 */
	public static final String UPLOAD_FILE_PATH = "/upload-files";
	public static final Set<String> SCRIPT_SUFFIX_SET = new HashSet<>(Arrays.asList("js", "css"));
	public static final Set<String> IMAGE_SUFFIX_SET = new HashSet<>(Arrays.asList("bmp", "jpg", "png", "jpeg", "gif"));
	public static final Set<String> VIDEO_SUFFIX_SET = new HashSet<>(Arrays.asList("mp4", "flv","avi","3gp","rm","rmvb","wmv","mov"));
	public static final Set<String> DOC_SUFFIX_SET = new HashSet<>(Arrays.asList("doc", "docx", "pdf","txt","html"));

	public final static  Long MAX_IMAGE_SIZE = 5 * 1024 * 1024L;
	public final static Long MAX_VIDEO_SIZE = 50 * 1024 * 1024L;
	public final static  Long MAX_DOC_SIZE = 5 * 1024 * 1024L;

	/**
	 *  异常类型
	 */
	public static final String SYSTEM_EXCEPTION = "系统异常";
	public static final String BUSINESS_EXCEPTION = "业务异常";

	/**
	 * token
	 */
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String TOKEN_REFRESH = "X-Refresh-Token";
	public static final String TOKEN_SECRET = "ThisisthetauruswwwSYSTEMsecret20200415";

	/**
	 * 换行符
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * 请求头
	 */
	public static final String X_REAL_IP = "X-Real-IP";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	/**
	 * 日志输出格式
	 */
	public static final String LOG_REQUEST_FORMAT = LINE_SEPARATOR + "@->> 接收请求日志:{}" ;
	public static final String LOG_RESPONSE_FORMAT = LINE_SEPARATOR+"<<-@ 响应请求日志:{}";
	public static final String LOG_EXCEPTION_BEFORE = LINE_SEPARATOR + "API日志拦截器before 出现异常.";
	public static final String LOG_EXCEPTION_AFTER_RETURNING = LINE_SEPARATOR + "API日志拦截器after returning 出现异常.";
	public static final String LOG_THROWING_AFTER_THROWING = LINE_SEPARATOR + "API日志拦截器after throwing 出现异常.";
	public static final String LOG_JWT_AUTHENTICATION_SUCCESS = LINE_SEPARATOR + "JWT鉴权成功 :";

	/**
	 * redis相关
	 */
	public final static String REDIS_GLOBAL = "global:";
	public final static String REDIS_UPDATE_COUNT = REDIS_GLOBAL + "update-count";

}
