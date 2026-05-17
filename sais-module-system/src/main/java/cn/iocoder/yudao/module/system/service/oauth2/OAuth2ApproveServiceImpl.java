package cn.iocoder.yudao.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import cn.iocoder.yudao.module.system.dal.mysql.oauth2.OAuth2ApproveMapper;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * OAuth2 approval Service implementation class
 *
 * @author Yudao Source Code
 */
@Service
@Validated
public class OAuth2ApproveServiceImpl implements OAuth2ApproveService {

    /**
     * Approval expiration time, default 30 days
     */
    private static final Integer TIMEOUT = 30 * 24 * 60 * 60; // Unit: seconds

    @Resource
    private OAuth2ClientService oauth2ClientService;

    @Resource
    private OAuth2ApproveMapper oauth2ApproveMapper;

    @Override
    @Transactional
    public boolean checkForPreApproval(Long userId, Integer userType, String clientId, Collection<String> requestedScopes) {
        // The first step is to calculate the automatic authorization based on the Client. If the scopes are all in automatic authorization, return true and pass
        OAuth2ClientDO clientDO = oauth2ClientService.validOAuthClientFromCache(clientId);
        Assert.notNull(clientDO, "Client cannot be empty"); // defensive programming
        if (CollUtil.containsAll(clientDO.getAutoApproveScopes(), requestedScopes)) {
            // gh-877 - if all scopes are auto approved, approvals still need to be added to the approval store.
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
            for (String scope : requestedScopes) {
                saveApprove(userId, userType, clientId, scope, true, expireTime);
            }
            return true;
        }

        // The second step is to count the authorizations that the user has approved. Returns true if scopes are included
        List<OAuth2ApproveDO> approveDOs = getApproveList(userId, userType, clientId);
        Set<String> scopes = convertSet(approveDOs, OAuth2ApproveDO::getScope,
                OAuth2ApproveDO::getApproved); // Only keep unexpired ones + Agree
        return CollUtil.containsAll(scopes, requestedScopes);
    }

    @Override
    @Transactional
    public boolean updateAfterApproval(Long userId, Integer userType, String clientId, Map<String, Boolean> requestedScopes) {
        // If requestedScopes is empty, indicating that there is no request, return true.
        if (CollUtil.isEmpty(requestedScopes)) {
            return true;
        }

        // Update approved information
        boolean success = false; // Requires at least one consent
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(TIMEOUT);
        for (Map.Entry<String, Boolean> entry : requestedScopes.entrySet()) {
            if (entry.getValue()) {
                success = true;
            }
            saveApprove(userId, userType, clientId, entry.getKey(), entry.getValue(), expireTime);
        }
        return success;
    }

    @Override
    public List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId) {
        List<OAuth2ApproveDO> approveDOs = oauth2ApproveMapper.selectListByUserIdAndUserTypeAndClientId(
                userId, userType, clientId);
        approveDOs.removeIf(o -> DateUtils.isExpired(o.getExpiresTime()));
        return approveDOs;
    }

    @VisibleForTesting
    void saveApprove(Long userId, Integer userType, String clientId,
                     String scope, Boolean approved, LocalDateTime expireTime) {
        // Update first
        OAuth2ApproveDO approveDO = new OAuth2ApproveDO().setUserId(userId).setUserType(userType)
                .setClientId(clientId).setScope(scope).setApproved(approved).setExpiresTime(expireTime);
        if (oauth2ApproveMapper.update(approveDO) == 1) {
            return;
        }
        // If it fails, it means it does not exist and is updated.
        oauth2ApproveMapper.insert(approveDO);
    }

}
