package com.code.taurus.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用VO: Id
 *
 * @author 郑楷山
 **/

@Data
public abstract class AbstractVO implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long fdId;

}
