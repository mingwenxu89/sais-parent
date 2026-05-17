package cn.iocoder.yudao.module.system.framework.operatelog.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * {@link IParseFunction} implementation class for administrator name
 *
 * @author HUIHUI
 */
@Slf4j
@Component
public class AdminUserParseFunction implements IParseFunction {

    public static final String NAME = "getAdminUserById";

    @Resource
    private AdminUserService adminUserService;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }

        // Get user information
        AdminUserDO user = adminUserService.getUser(Convert.toLong(value));
        if (user == null) {
            log.warn("[apply][Get user{{}} is empty", value);
            return "";
        }
        // Return format taro source code (13888888888)
        String nickname = user.getNickname();
        if (StrUtil.isEmpty(user.getMobile())) {
            return nickname;
        }
        return StrUtil.format("{}({})", nickname, user.getMobile());
    }

}
