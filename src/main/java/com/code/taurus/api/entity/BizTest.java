package com.code.taurus.api.entity;

import com.code.taurus.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实体： 测试
 *
 * @author 郑楷山
 **/

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Entity
@Data
@Table
public class BizTest extends AbstractEntity {

    @Column(length = 128, nullable = false)
    private String fdName;

}
