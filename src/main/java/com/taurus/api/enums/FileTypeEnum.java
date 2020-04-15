package com.taurus.api.enums;


import com.taurus.common.constant.CommonConstant;
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

    // 首页轮播图片
    IMAGE_HOME_PAGE_BANNER("","image",CommonConstant.MAX_IMAGE_SIZE,CommonConstant.HOME_PAGE_BANNER_PATH),

            ;

    private String name;
    private String type; 
    private Long size;
    private String path;
}
