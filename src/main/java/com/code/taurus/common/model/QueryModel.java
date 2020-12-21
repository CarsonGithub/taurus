package com.code.taurus.common.model;

import com.code.taurus.common.enums.ConditionEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用查询model
 *
 * @author 郑楷山
 **/

/*
传参格式:
1. isPage默认为true,为false时忽略offset和pageSize
2. ignoreColumns优先级高于columns
{
    "columns": ["fdId","name","infoSet"],
    "ignoreColumns": ["infoSet"],
    "conditions": {
        "infoSet.id": {
            "EQUAL": "30"
        }
    },
    "isPaged": false,
    "offset": 1,
    "pageSize": 1,
    "sorts": {
        "order": "DESC"
    }
}
 */

@Accessors(chain = true)
@Data
public class QueryModel {

    @ApiParam(value = "当前页面", example = "1")
    private Integer offset;

    @ApiParam(value = "页面大小", example = "10")
    private Integer pageSize;

    @ApiParam(value = "展示字段")
    private List<String> columns;

    @ApiParam(value = "忽略字段")
    private List<String> ignoreColumns;

    @ApiParam(value = "排序字段")
    private Map<String, String> sorts;

    @ApiParam(value = "条件字段")
    private Map<String, Object> conditions;

    @ApiParam(value = "是否分页(可不传,默认true,表示分页)")
    private Boolean isPaged;

    /**
     * 添加EQ条件
     */
    public QueryModel addCondition(String column, Object value) {
        return addCondition(column, ConditionEnum.EQUAL, value);
    }

    /**
     * 添加条件
     */
    @SuppressWarnings("unchecked")
    public QueryModel addCondition(String column, ConditionEnum opt,
                                   Object value) {
        Map<String, Object> map;
        if (this.conditions == null) {
            this.conditions = new LinkedHashMap<>();
            map = new LinkedHashMap<>(16);
            this.conditions.put(column, map);
        } else {
            Object val = this.conditions.get(column);
            if (val == null) {
                map = new LinkedHashMap<>();
                this.conditions.put(column, map);
            } else if (val instanceof Map) {
                map = (Map<String, Object>) val;
            } else {
                map = new LinkedHashMap<>(16);
                this.conditions.put(column, map);
                map.put(ConditionEnum.EQUAL.getValue(), val);
            }
        }
        map.put(opt.getValue(), value);
        return this;
    }

    /**
     * 添加排序
     */
    public QueryModel addSort(String column, Sort.Direction direction) {
        if (this.sorts == null) {
            this.sorts = new LinkedHashMap<>();
            this.sorts.put(column, direction.name());
        } else {
            if (!this.sorts.containsKey(column)) {
                this.sorts.put(column, direction.name());
            }
        }
        return this;
    }

    /**
     * 添加字段
     */
    public QueryModel addColumns(String... cols) {
        return addColumns(Arrays.stream(cols).collect(Collectors.toList()));
    }

    /**
     * 添加字段
     */
    public QueryModel addColumns(List<String> cols) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        columns.addAll(cols);
        return this;
    }

    /**
     * 添加忽略字段
     * >> 优先级最高
     */
    public QueryModel addIgnoreColumns(String... cols) {
        return addIgnoreColumns(Arrays.stream(cols).collect(Collectors.toList()));
    }

    /**
     * 添加忽略字段
     * >> 优先级最高
     */
    public QueryModel addIgnoreColumns(List<String> cols) {
        if (this.ignoreColumns == null) {
            this.ignoreColumns = new ArrayList<>();
        }
        ignoreColumns.addAll(cols.stream().distinct().collect(Collectors.toList()));
        return this;
    }

}
