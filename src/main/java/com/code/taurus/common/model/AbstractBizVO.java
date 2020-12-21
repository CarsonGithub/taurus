package com.code.taurus.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用业务VO
 *
 * @author 郑楷山
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractBizVO extends AbstractVO {

    protected Date fdCreateTime;

    protected Date fdUpdateTime;

    protected String fdCreateBy;

    protected String fdUpdateBy;

}


