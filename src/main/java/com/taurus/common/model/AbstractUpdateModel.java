package com.taurus.common.model;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Positive;

/**
 * 通用查询model
 *
 * @author 郑楷山
 **/

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public abstract class AbstractUpdateModel extends AbstractModel {

    @Positive
    @ApiParam(value = "ID"  , required = true)
    private Long fdId;

}
