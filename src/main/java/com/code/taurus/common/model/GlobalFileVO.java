package com.code.taurus.common.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模型： 全局文件上传返回数据
 *
 * @author 郑楷山
 **/


@ApiModel(value = "文件上传: 数据模型")
@Data
@Accessors(chain = true)
public class GlobalFileVO {

    @ApiParam(value = "文件名")
    private String fdFileName;

    @ApiParam(value = "文件路径")
    private String fdFilePath;
}
