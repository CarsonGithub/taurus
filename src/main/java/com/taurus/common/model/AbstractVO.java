package com.taurus.common.model;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用VO: Id自增
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
public abstract class AbstractVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiParam(value = "ID" )
    private Long fdId;
}
