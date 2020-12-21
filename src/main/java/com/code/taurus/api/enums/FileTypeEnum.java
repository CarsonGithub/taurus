package com.code.taurus.api.enums;


import com.code.taurus.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举: 文件类型
 *
 * @author 郑楷山
 **/

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    // 用户头像
    USER_IMAGE_ICON("", "image", CommonConstant.MAX_IMAGE_SIZE, CommonConstant.USER_IMAGE_ICON),
    ;

    private final String name;
    private final String type;
    private final Long size;
    private final String path;
}
