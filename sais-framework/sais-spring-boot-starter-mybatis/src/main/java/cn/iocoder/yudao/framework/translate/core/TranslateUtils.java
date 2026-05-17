package cn.iocoder.yudao.framework.translate.core;

import cn.hutool.core.collection.CollUtil;
import com.fhs.core.trans.vo.VO;
import com.fhs.trans.service.impl.TransService;

import java.util.List;

/**
 * VO Data Translation Utils
 *
 * @author Yudao Source Code
 */
public class TranslateUtils {

 private static TransService transService;

 public static void init(TransService transService) {
 TranslateUtils.transService = transService;
 }

 /**
     * Data translation
 *
     * Usage scenarios: Scenarios where @TransMethodResult annotation cannot be used and translation can only be triggered manually
 *
     * @param data data
     * @return Translation results
 */
 public static <T extends VO> List<T> translate(List<T> data) {
 if (CollUtil.isNotEmpty((data))) {
 transService.transBatch(data);
 }
 return data;
 }

}
