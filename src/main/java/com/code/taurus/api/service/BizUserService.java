package com.code.taurus.api.service;

import com.code.taurus.api.entity.BizUser;
import com.code.taurus.api.model.BizUserVO;
import com.code.taurus.common.enums.ConditionEnum;
import com.code.taurus.common.model.NoRecordException;
import com.code.taurus.common.model.QueryModel;
import com.code.taurus.common.service.impl.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户操作
 *
 * @author 郑楷山
 */

@Transactional(readOnly = true)
@Service
public class BizUserService extends AbstractService<BizUser, BizUserVO> {

    public BizUserService() {
        super(BizUser.class, BizUserVO.class);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public BizUserVO getByName(String name) {
        QueryModel queryModel = new QueryModel();
        queryModel.addCondition("fdName", ConditionEnum.EQUAL, name);
        return super.findOneVO(queryModel).orElseThrow(NoRecordException::new);
    }

    @Override
    protected void beforeCreateOrUpdate(BizUser entity, boolean isCreate) {
        super.beforeCreateOrUpdate(entity, isCreate);
        if (StringUtils.isNotBlank(entity.getFdPassword())) {
            entity.setFdPassword(passwordEncoder.encode(entity.getFdPassword()));
        }
    }
}
