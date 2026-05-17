package cn.iocoder.yudao.module.system.framework.operatelog.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * {@link IParseFunction} implementation class of department name
 *
 * @author HUIHUI
 */
@Slf4j
@Component
public class DeptParseFunction implements IParseFunction {

    public static final String NAME = "getDeptById";

    @Resource
    private DeptService deptService;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }

        // Get department information
        DeptDO dept = deptService.getDept(Convert.toLong(value));
        if (dept == null) {
            log.warn("[apply][Get department {{}} is empty", value);
            return "";
        }
        return dept.getName();
    }

}
