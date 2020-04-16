package com.taurus.common.model;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 通用model
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
public abstract class AbstractVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Positive
    @ApiParam(value = "ID"  , required = true)
    private Long fdId;
}
