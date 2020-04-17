package com.taurus.api.model;

import com.taurus.api.enums.RoleEnum;
import com.taurus.common.model.AbstractVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 模型： 用户新增
 *
 * @author 郑楷山
 **/

@ApiModel(value = "用户:新增模型")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BizUserVO extends AbstractVO {

    @NotBlank
    @Size(max = 8)
    @ApiParam(value = "用户名", example = "admin", required = true)
    private String fdName;

    @Size(max = 16,min = 6)
    @ApiParam(value = "密码",example = "123456" , required = true)
    private String fdPassword;

    @Size(max = 64)
    @ApiParam(value = "邮箱",example = "test@taurus.com.cn" , required = true)
    private String fdEmail;

    @ApiParam(value = "角色",example = "USER" )
    private RoleEnum fdRole;

}
