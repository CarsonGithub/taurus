package com.code.taurus.api.entity;

import com.code.taurus.api.enums.RoleEnum;
import com.code.taurus.common.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 实体： 用户
 *
 * @author 郑楷山
 **/
@EqualsAndHashCode(callSuper = true)
@Entity
@Accessors(chain = true)
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"fdName"})
)
public class BizUser extends AbstractEntity {

    @Override
    public void beforeCreate() {
        super.beforeCreate();
        if (fdRole == null) {
            fdRole = RoleEnum.USER;
        }
    }

    @Column(length = 32, nullable = false)
    private String fdName;

    @Column(length = 128, nullable = false)
    private String fdPassword;

    @Column(length = 64)
    private String fdEmail;
    /**
     * 0:管理员, 1商户,2用户
     */
    @Column(columnDefinition = "int(2)", nullable = false)
    private RoleEnum fdRole;

}
