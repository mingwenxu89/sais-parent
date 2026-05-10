package cn.iocoder.yudao.module.agri.dal.mysql.irrigation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.agri.controller.admin.irrigation.vo.IrrigationPlanPageReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.irrigation.IrrigationPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface IrrigationPlanMapper extends BaseMapperX<IrrigationPlanDO> {

    /** PENDING plans whose planned start time has arrived — ready to be dispatched via MQTT */
    default List<IrrigationPlanDO> selectPendingDuePlans(LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eq(IrrigationPlanDO::getStatus, "PENDING")
                .le(IrrigationPlanDO::getPlannedStartTime, now));
    }

    /** The EXECUTING plan for a device (used by stopWatering and device detail display) */
    default IrrigationPlanDO selectExecutingByDeviceId(Long deviceId) {
        return selectOne(new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eq(IrrigationPlanDO::getDeviceId, deviceId)
                .eq(IrrigationPlanDO::getStatus, "EXECUTING")
                .orderByDesc(IrrigationPlanDO::getId)
                .last("LIMIT 1"));
    }

    /** EXECUTING plans whose planned duration has elapsed — ready to be marked COMPLETED */
    default List<IrrigationPlanDO> selectExecutingTimedOutPlans(LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eq(IrrigationPlanDO::getStatus, "EXECUTING")
                .isNotNull(IrrigationPlanDO::getActualStartTime)
                .apply("actual_start_time + (planned_duration * interval '1 minute') <= {0}", now));
    }

    /** EXECUTING plans that have been running longer than the ACK timeout and still have no device acknowledgement */
    default List<IrrigationPlanDO> selectExecutingAckTimeoutPlans(LocalDateTime cutoff) {
        return selectList(new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eq(IrrigationPlanDO::getStatus, "EXECUTING")
                .isNull(IrrigationPlanDO::getAckReceivedAt)
                .le(IrrigationPlanDO::getActualStartTime, cutoff));
    }

    /** EXECUTING plans that have not yet received a device ACK (for mock job polling) */
    default List<IrrigationPlanDO> selectExecutingWithoutAck() {
        return selectList(new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eq(IrrigationPlanDO::getStatus, "EXECUTING")
                .isNull(IrrigationPlanDO::getAckReceivedAt)
                .isNotNull(IrrigationPlanDO::getActualStartTime));
    }

    default PageResult<IrrigationPlanDO> selectPage(IrrigationPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IrrigationPlanDO>()
                .eqIfPresent(IrrigationPlanDO::getFieldId, reqVO.getFieldId())
                .eqIfPresent(IrrigationPlanDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(IrrigationPlanDO::getDecisionSource, reqVO.getDecisionSource())
                .eqIfPresent(IrrigationPlanDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(IrrigationPlanDO::getPlannedStartTime, reqVO.getPlannedStartTime())
                .orderByDesc(IrrigationPlanDO::getId));
    }

}
