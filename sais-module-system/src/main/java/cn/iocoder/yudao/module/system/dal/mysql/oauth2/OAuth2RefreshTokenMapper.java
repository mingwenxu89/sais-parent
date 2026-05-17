package cn.iocoder.yudao.module.system.dal.mysql.oauth2;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2RefreshTokenDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2RefreshTokenMapper extends BaseMapperX<OAuth2RefreshTokenDO> {

    default int deleteByRefreshToken(String refreshToken) {
        return delete(new LambdaQueryWrapperX<OAuth2RefreshTokenDO>()
                .eq(OAuth2RefreshTokenDO::getRefreshToken, refreshToken));
    }

    @TenantIgnore // When obtaining the token, you need to ignore the tenant ID. The reason is: In some scenarios, the tenant-id request header may not be passed, such as file upload, building block report, etc.
    default OAuth2RefreshTokenDO selectByRefreshToken(String refreshToken) {
        return selectOne(OAuth2RefreshTokenDO::getRefreshToken, refreshToken);
    }

}
