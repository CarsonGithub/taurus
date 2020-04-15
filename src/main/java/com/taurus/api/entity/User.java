package com.taurus.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taurus.api.enums.RoleEnum;
import com.taurus.common.entity.AbstractEntity;
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
        name="tb_user",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fdName"})
)
public class User extends AbstractEntity {

    @Column(length = 8,nullable = false)
    private String fdName;

    @Column(length = 128,nullable = false)
    @JsonIgnore
    private String fdPassword;

    @Column(length = 64,nullable = false)
    private String fdEmail;

    @Column(columnDefinition = "int(2)", nullable = false)
    private RoleEnum fdRole;

}
