package com.code.taurus.api.model;

import com.code.taurus.common.model.AbstractBizVO;
import com.code.taurus.common.model.AbstractVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 模型： 测试 新增
 *
 * @author 郑楷山
 **/

@ApiModel(value = "测试:新增模型")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BizTestVO extends AbstractBizVO {

    @ApiParam(value = "名称", example = "阿花")
    private String fdName;


}
