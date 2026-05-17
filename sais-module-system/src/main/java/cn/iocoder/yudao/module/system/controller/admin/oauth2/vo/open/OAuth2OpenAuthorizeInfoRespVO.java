package cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.open;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Management backend - authorization page information Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2OpenAuthorizeInfoRespVO {

    /**
     * Client
     */
    private Client client;

    @Schema(description = "For scope selection information, use List to ensure orderliness. Key is scope, and Value is whether it is selected.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<KeyValue<String, Boolean>> scopes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {

        @Schema(description = "Application name", requiredMode = Schema.RequiredMode.REQUIRED, example = "potatoes")
        private String name;

        @Schema(description = "application icon", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
        private String logo;

    }

}
