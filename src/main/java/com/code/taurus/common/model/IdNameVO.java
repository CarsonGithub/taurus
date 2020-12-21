package com.code.taurus.common.model;

import com.code.taurus.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型： 测试 新增
 *
 * @author 郑楷山
 **/

@Data
@EqualsAndHashCode(callSuper = true)
public class IdNameVO extends AbstractVO {

    private String fdName;

    public static <E extends AbstractEntity> IdNameVO of(E entity) {
        return IdNameVO.of(entity.getFdId());
    }

    public static IdNameVO of(String id) {
        return IdNameVO.of(Long.valueOf(id));
    }

    public static IdNameVO of(Long id) {
        IdNameVO idNameVO = new IdNameVO();
        idNameVO.setFdId(id);
        return idNameVO;
    }

    public static IdNameVO of(Long id, String name) {
        IdNameVO idNameVO = IdNameVO.of(id);
        idNameVO.setFdName(name);
        return idNameVO;
    }

}
