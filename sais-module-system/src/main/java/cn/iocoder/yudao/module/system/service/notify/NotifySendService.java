package cn.iocoder.yudao.module.system.service.notify;

import java.util.List;
import java.util.Map;

/**
 * Service API for sending intra-site messages
 *
 * @author xrcoder
 */
public interface NotifySendService {

    /**
     * Send a single site message to users in the management backend
     *
     * When mobile is empty, use userId to load the mobile phone ID of the corresponding administrator.
     *
     * @param userId User ID
     * @param templateCode SMS template ID
     * @param templateParams SMS template parameters
     * @return Send log ID
     */
    Long sendSingleNotifyToAdmin(Long userId,
                                 String templateCode, Map<String, Object> templateParams);
    /**
     * Send a single site message to users of the user APP
     *
     * When mobile is empty, use userId to load the mobile phone ID of the corresponding member.
     *
     * @param userId User ID
     * @param templateCode Site letter template ID
     * @param templateParams Site letter template parameters
     * @return Send log ID
     */
    Long sendSingleNotifyToMember(Long userId,
                                  String templateCode, Map<String, Object> templateParams);

    /**
     * Send a single site message to the user
     *
     * @param userId User ID
     * @param userType User type
     * @param templateCode Site letter template ID
     * @param templateParams Site letter template parameters
     * @return Send log ID
     */
    Long sendSingleNotify( Long userId, Integer userType,
                           String templateCode, Map<String, Object> templateParams);

    default void sendBatchNotify(List<String> mobiles, List<Long> userIds, Integer userType,
                                 String templateCode, Map<String, Object> templateParams) {
        throw new UnsupportedOperationException("This operation is not supported for the time being. If you are interested, you can implement this function!");
    }

}
