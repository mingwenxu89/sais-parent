package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2ApproveDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OAuth2 approval Service API
 *
 * Functionally, it is similar to the function of Spring Security OAuth's ApprovalStoreUserApprovalHandler, recording the user's authorization for the specified client, reducing manual determination.
 *
 * @author Yudao Source Code
 */
public interface OAuth2ApproveService {

    /**
     * Obtain the specified authorization for the specified user and the specified client, whether it is passed
     *
     * Refer to the checkForPreApproval method of ApprovalStoreUserApprovalHandler
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param requestedScopes Authorization scope
     * @return Is authorization passed?
     */
    boolean checkForPreApproval(Long userId, Integer userType, String clientId, Collection<String> requestedScopes);

    /**
     * When the user initiates approval, based on the options of scopes, calculate whether the final approval is passed.
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @param requestedScopes Authorization scope
     * @return Is authorization passed?
     */
    boolean updateAfterApproval(Long userId, Integer userType, String clientId, Map<String, Boolean> requestedScopes);

    /**
     * Get user's approved list, exclude expired ones
     *
     * @param userId User ID
     * @param userType User type
     * @param clientId client ID
     * @return Is authorization passed?
     */
    List<OAuth2ApproveDO> getApproveList(Long userId, Integer userType, String clientId);

}
