package cn.iocoder.yudao.module.system.framework.operatelog.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.PostDO;
import cn.iocoder.yudao.module.system.service.dept.PostService;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * {@link IParseFunction} implementation class of job name
 *
 * @author HUIHUI
 */
@Slf4j
@Component
public class PostParseFunction implements IParseFunction {

    public static final String NAME = "getPostById";

    @Resource
    private PostService postService;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }

        // Get job information
        PostDO post = postService.getPost(Convert.toLong(value));
        if (post == null) {
            log.warn("[apply][Get position{{}} is empty", value);
            return "";
        }
        return post.getName();
    }

}
