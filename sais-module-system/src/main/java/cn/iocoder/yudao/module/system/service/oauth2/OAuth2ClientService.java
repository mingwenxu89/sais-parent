package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client.OAuth2ClientPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.client.OAuth2ClientSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2ClientDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * OAuth2.0 Client Service API
 *
 * Functionally, and the function of JdbcClientDetailsService, it provides client operations
 *
 * @author Yudao Source Code
 */
public interface OAuth2ClientService {

    /**
     * Create an OAuth2 client
     *
     * @param createReqVO Create information
     * @return ID
     */
    Long createOAuth2Client(@Valid OAuth2ClientSaveReqVO createReqVO);

    /**
     * Update OAuth2 client
     *
     * @param updateReqVO Update information
     */
    void updateOAuth2Client(@Valid OAuth2ClientSaveReqVO updateReqVO);

    /**
     * Remove OAuth2 client
     *
     * @param id ID
     */
    void deleteOAuth2Client(Long id);

    /**
     * Batch deletion of OAuth2 clients
     *
     * @param ids IDed array
     */
    void deleteOAuth2ClientList(List<Long> ids);

    /**
     * Get the OAuth2 client
     *
     * @param id ID
     * @return OAuth2 client
     */
    OAuth2ClientDO getOAuth2Client(Long id);

    /**
     * Get the OAuth2 client from cache
     *
     * @param clientId client ID
     * @return OAuth2 client
     */
    OAuth2ClientDO getOAuth2ClientFromCache(String clientId);

    /**
     * Get OAuth2 client pagination
     *
     * @param pageReqVO Page query
     * @return OAuth2 client pagination
     */
    PageResult<OAuth2ClientDO> getOAuth2ClientPage(OAuth2ClientPageReqVO pageReqVO);

    /**
     * From the cache, verify whether the client is legitimate
     *
     * @return Client
     */
    default OAuth2ClientDO validOAuthClientFromCache(String clientId) {
        return validOAuthClientFromCache(clientId, null, null, null, null);
    }

    /**
     * From the cache, verify whether the client is legitimate
     *
     * When it is not empty, perform verification
     *
     * @param clientId client ID
     * @param clientSecret client key
     * @param authorizedGrantType Authorization method
     * @param scopes Authorization scope
     * @param redirectUri redirect address
     * @return Client
     */
    OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType,
                                             Collection<String> scopes, String redirectUri);

}
