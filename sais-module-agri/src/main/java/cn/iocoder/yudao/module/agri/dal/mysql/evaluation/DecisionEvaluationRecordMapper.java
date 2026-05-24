package cn.iocoder.yudao.module.agri.dal.mysql.evaluation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.evaluation.vo.DecisionEvaluationPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.evaluation.DecisionEvaluationRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DecisionEvaluationRecordMapper extends BaseMapperX<DecisionEvaluationRecordDO> {

    default PageResult<DecisionEvaluationRecordDO> selectPage(DecisionEvaluationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DecisionEvaluationRecordDO>()
                .eqIfPresent(DecisionEvaluationRecordDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(DecisionEvaluationRecordDO::getAligned, reqVO.getAligned())
                .orderByDesc(DecisionEvaluationRecordDO::getId));
    }

}
