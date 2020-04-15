package com.taurus.api.model;

import com.taurus.common.model.AbstractUpdateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 模型： 测试 编辑
 *
 * @author 郑楷山
 **/

@ApiModel(value = "测试:编辑模型")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TestUpdateModel extends AbstractUpdateModel {

    @ApiParam(value = "名称")
    private String fdName;


}
