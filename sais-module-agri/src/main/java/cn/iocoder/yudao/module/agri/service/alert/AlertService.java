package cn.iocoder.yudao.module.agri.service.alert;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertPageReqVO;
import cn.iocoder.yudao.module.agri.controller.admin.alert.vo.AlertSaveReqVO;
import cn.iocoder.yudao.module.agri.dal.dataobject.alert.AlertDO;

import jakarta.validation.Valid;

/**
 * Alert Service interface
 */
public interface AlertService {

    Long createAlert(@Valid AlertSaveReqVO createReqVO);

    void updateAlert(@Valid AlertSaveReqVO updateReqVO);

    void deleteAlert(Long id);

    AlertDO getAlert(Long id);

    PageResult<AlertDO> getAlertPage(AlertPageReqVO pageReqVO);

    /**
     * Raises a system-generated alert. Caller is responsible for dedup check via hasActiveAlert().
     */
    void raiseAlert(String alertType, String level, Long farmId, Long fieldId,
                    Long irrigationPlanId, String context);

    /**
     * Updates the status of an alert (1=HANDLING / 2=RESOLVED / 3=IGNORED) and records handled time.
     */
    void handleAlert(Long id, Integer status);

    /**
     * Returns true if an unresolved alert of the given type already exists for the farm/field.
     */
    boolean hasActiveAlert(String alertType, Long farmId, Long fieldId);

}
