package cn.iocoder.yudao.module.infra.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static cn.hutool.core.util.ArrayUtil.*;

/**
 * Code-generated scenario enumeration
 *
 * @author Yudao Source Code
 */
@AllArgsConstructor
@Getter
public enum CodegenSceneEnum {

    ADMIN(1, "Admin Backend", "admin", ""),
    APP(2, "User App", "app", "App");

    /**
     * scene
     */
    private final Integer scene;
    /**
     * scene name
     */
    private final String name;
    /**
     * Basic package name
     */
    private final String basePackage;
    /**
     * Prefixes for Controller and VO classes
     */
    private final String prefixClass;

    public static CodegenSceneEnum valueOf(Integer scene) {
        return firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene), values());
    }

}
