package cn.iocoder.yudao.module.infra.enums.codegen;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Code Generator Fields HTML Display Enum
 */
@AllArgsConstructor
@Getter
public enum CodegenColumnHtmlTypeEnum {

    INPUT("input"), // Text input
    TEXTAREA("textarea"), // Textarea
    SELECT("select"), // Select box
    RADIO("radio"), // Radio button
    CHECKBOX("checkbox"), // Checkbox
    DATETIME("datetime"), // Date control
    IMAGE_UPLOAD("imageUpload"), // Upload image
    FILE_UPLOAD("fileUpload"), // Upload file
    EDITOR("editor"), // Rich text control
    ;

    /**
     * Conditions
     */
    private final String type;

}
