package cn.iocoder.yudao.module.system.service.mail;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.account.MailAccountPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.mail.vo.account.MailAccountSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.mail.MailAccountDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Email account Service API
 *
 * @author wangjingyi
 * @since 2022-03-21
 */
public interface MailAccountService {

    /**
     * Create an email account
     *
     * @param createReqVO Email account information
     * @return ID
     */
    Long createMailAccount(@Valid MailAccountSaveReqVO createReqVO);

    /**
     * Modify email account
     *
     * @param updateReqVO Email account information
     */
    void updateMailAccount(@Valid MailAccountSaveReqVO updateReqVO);

    /**
     * Delete email account
     *
     * @param id ID
     */
    void deleteMailAccount(Long id);

    /**
     * Delete email accounts in batches
     *
     * @param ids ID list
     */
    void deleteMailAccountList(List<Long> ids);

    /**
     * Get email account information
     *
     * @param id ID
     * @return Email account information
     */
    MailAccountDO getMailAccount(Long id);

    /**
     * Get email account from cache
     *
     * @param id ID
     * @return Email account
     */
    MailAccountDO getMailAccountFromCache(Long id);

    /**
     * Get email account pagination information
     *
     * @param pageReqVO Email account paging parameters
     * @return Email account paging information
     */
    PageResult<MailAccountDO> getMailAccountPage(MailAccountPageReqVO pageReqVO);

    /**
     * Get mailbox array information
     *
     * @return Email account information array
     */
    List<MailAccountDO> getMailAccountList();

}
