package com.taurus.api.model;

import com.taurus.common.model.AbstractUpdateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

/**
 * 模型： 用户更新
 *
 * @author 郑楷山
 **/

@ApiModel(value = "用户:更新模型")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserUpdateModel extends AbstractUpdateModel {

    @Size(max = 8)
    @ApiParam(value = "用户名", example = "admin")
    private String name;

    @Size(max = 64)
    @ApiParam(value = "邮箱",example = "test@taurus.com.cn" )
    private String email;


    @ApiParam(value = "备注",example = "我什么都不想说!" )
    private String remark;

}
