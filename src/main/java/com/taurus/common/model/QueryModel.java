package com.taurus.common.model;

import com.taurus.common.enums.ConditionEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 通用查询model
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
public class QueryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiParam(value = "当前页面", example = "1")
    private int offset = 1;

    @ApiParam(value = "页面大小", example = "10")
    private int pageSize = 10;

    @ApiParam(value = "检索字段")
    private List<String> columns;

    @ApiParam(value = "排序字段")
    private EnumMap<Sort.Direction,String> sorts;

    @ApiParam(value = "检索字段")
    private Map<String,EnumMap<ConditionEnum,Object>> conditions;

    @ApiParam(value = "是否分页(可不传,默认true,表示需要分页)")
    private boolean isPaged = true;

}
