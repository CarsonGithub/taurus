package com.code.taurus.api.controller.backend;

import com.code.taurus.api.entity.BizUser;
import com.code.taurus.api.model.BizUserVO;
import com.code.taurus.api.service.BizUserService;
import com.code.taurus.common.config.security.JWTAuthUtil;
import com.code.taurus.common.config.security.UserContextHelper;
import com.code.taurus.common.controller.AbstractController;
import com.code.taurus.common.model.NoRecordException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 控制器： 用户
 *
 * @author 郑楷山
 **/

@Slf4j
@Api(tags = "Backend: 用户")
@RestController
@RequestMapping("/data/user")
public class UserController extends AbstractController<BizUserService, BizUser, BizUserVO> {

    @Autowired
    private JWTAuthUtil jwtAuthUtil;

    @ApiOperation("重置密码")
    @PostMapping("/reset/{id}/{password}")
    public void reset(@PathVariable @ApiParam(value = "ID", example = "1", required = true) Long id,
                      @PathVariable @ApiParam(value = "新密码", example = "123123", required = true) String password) {
        BizUser user = new BizUser();
        user.setFdId(id);
        user.setFdPassword(password);
        getApi().update(user);
    }

    @ApiOperation("获取当前用户")
    @PostMapping("/getCurrentUser")
    public BizUserVO getCurrentUser() {
        return UserContextHelper.getUser().orElseThrow(NoRecordException::new);
    }

    @ApiOperation("刷新token")
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletResponse httpServletResponse) {
        jwtAuthUtil.buildResponseTokenHeader(httpServletResponse, UserContextHelper.getUser().orElseThrow(NoRecordException::new));
    }
}
