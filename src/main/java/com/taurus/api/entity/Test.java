package com.taurus.api.entity;

import com.taurus.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
//@ToString(exclude = {"test"})
//@EqualsAndHashCode(exclude = {"test"}, callSuper = true)
@Table(name = "tb_test")
public class Test extends AbstractEntity {


}
