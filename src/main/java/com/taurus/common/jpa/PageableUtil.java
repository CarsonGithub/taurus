package com.taurus.common.jpa;

import com.taurus.common.model.QueryModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * 帮助类: 页面条件拼接
 *
 * @author 郑楷山
 **/

public class PageableUtil {

    /**
     *  页面排序
     * @param queryModel
     * @return
     */
    public static Pageable build(QueryModel queryModel) {
        return PageRequest
                .of(queryModel.getOffset(),
                        queryModel.getPageSize() == 0 ? 10 : queryModel.getPageSize(),
                        getSort(queryModel))
                .previous();
    }

    /**
     * 获取排序条件
     * @param queryModel
     * @return
     */
    private static Sort getSort(QueryModel queryModel) {
        EnumMap<Sort.Direction, String> sorts = queryModel.getSorts();
        List<Sort.Order> orderList = new ArrayList<>();
        if (sorts != null && !sorts.isEmpty()) {
            sorts.forEach((k,v)-> orderList.add(new Sort.Order(k, v)));
        }
        return Sort.by(orderList);
    }

}
