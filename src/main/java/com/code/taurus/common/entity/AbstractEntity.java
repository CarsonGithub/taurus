package com.code.taurus.common.entity;

import com.code.taurus.common.config.security.UserContextHelper;
import com.code.taurus.common.model.SysSecurityUser;
import com.code.taurus.common.service.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * 实体/表 基类
 *
 * @author 郑楷山
 **/

@Accessors(chain = true)
@Data
@MappedSuperclass
public abstract class AbstractEntity implements IEntity, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void beforeCreate() {
        Date date = new Date();
        Optional<SysSecurityUser> optional = UserContextHelper.getUser();
        this.fdCreateBy = optional.isPresent() ? optional.get().getUsername() : "";
        this.fdCreateTime = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fdId;

    @Column(length = 32)
    private String fdCreateBy;

    @Column(nullable = false)
    private Date fdCreateTime;

    @Column(length = 32)
    private String fdUpdateBy;

    @Column(nullable = false)
    private Date fdUpdateTime;

}
