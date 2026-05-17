package cn.iocoder.yudao.module.system.service.member;

/**
 * Member Service API
 *
 * @author Yudao Source Code
 */
public interface MemberService {

    /**
     * Obtain the mobile phone ID of the member user
     *
     * @param id Member user ID
     * @return Mobile phone ID
     */
    String getMemberUserMobile(Long id);

    /**
     * Obtain member user's email address
     *
     * @param id Member user ID
     * @return Email
     */
    String getMemberUserEmail(Long id);

}
