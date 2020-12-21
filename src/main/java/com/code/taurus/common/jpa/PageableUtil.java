package com.code.taurus.common.jpa;

import com.code.taurus.common.model.QueryModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帮助类: 页面条件拼接
 *
 * @author 郑楷山
 **/

public class PageableUtil {

    /**
     * 页面排序
     */
    public static Pageable build(QueryModel queryModel) {
        if (queryModel.getIsPaged() == null) {
            queryModel.setIsPaged(true);
        }
        if (!queryModel.getIsPaged()) {
            queryModel.setOffset(1);
            queryModel.setPageSize(1000);
        }
        Integer offset = queryModel.getOffset();
        Integer pageSize = queryModel.getPageSize();
        queryModel.setOffset(offset == null || offset == 0 ? 1 : offset);
        queryModel.setPageSize(pageSize == null || pageSize == 0 ? 5 : pageSize);
        return PageRequest.of(queryModel.getOffset(), queryModel.getPageSize(), getSort(queryModel)).previous();
    }

    /**
     * 获取排序条件
     */
    private static Sort getSort(QueryModel queryModel) {
        Map<String, String> sorts = queryModel.getSorts();
        List<Sort.Order> sortList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sorts)) {
            sorts.forEach((k, v) -> sortList.add(new Sort.Order(Sort.Direction.fromString(v), k)));
        }
        return Sort.by(sortList);
    }

}
