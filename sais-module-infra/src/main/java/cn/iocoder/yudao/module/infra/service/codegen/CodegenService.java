package cn.iocoder.yudao.module.infra.service.codegen;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenCreateListReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.CodegenUpdateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.CodegenTablePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.codegen.vo.table.DatabaseTableRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenColumnDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.codegen.CodegenTableDO;

import java.util.List;
import java.util.Map;

/**
 * Code generation service API
 *
 * @author Yudao Source Code
 */
public interface CodegenService {

    /**
     * Based on the table structure of the database, create the table definition of the code generator
     *
     * @param author Author
     * @param reqVO  Table information
     * @return IDed array of table definitions created
     */
    List<Long> createCodegenList(String author, CodegenCreateListReqVO reqVO);

    /**
     * Update database table and field definitions
     *
     * @param updateReqVO Update information
     */
    void updateCodegen(CodegenUpdateReqVO updateReqVO);

    /**
     * Based on the table structure of the database, synchronize the table and field definitions of the database
     *
     * @param tableId table ID
     */
    void syncCodegenFromDB(Long tableId);

    /**
     * Delete table and field definitions from the database
     *
     * @param tableId Data ID
     */
    void deleteCodegen(Long tableId);

    /**
     * Delete database table and field definitions in batches
     *
     * @param tableIds Data ID list
     */
    void deleteCodegenList(List<Long> tableIds);

    /**
     * Get a list of table definitions
     *
     * @param dataSourceConfigId Data source configuration ID
     * @return table definition list
     */
    List<CodegenTableDO> getCodegenTableList(Long dataSourceConfigId);

    /**
     * Get table definition pagination
     *
     * @param pageReqVO Paging conditions
     * @return table definition pagination
     */
    PageResult<CodegenTableDO> getCodegenTablePage(CodegenTablePageReqVO pageReqVO);

    /**
     * Get table definition
     *
     * @param id table ID
     * @return table definition
     */
    CodegenTableDO getCodegenTable(Long id);

    /**
     * Get the field definition array of the specified table
     *
     * @param tableId table ID
     * @return Field definition array
     */
    List<CodegenColumnDO> getCodegenColumnListByTableId(Long tableId);

    /**
     * Perform code generation for the specified table
     *
     * @param tableId table ID
     * @return Generate results. key is the file path, value is the corresponding code content
     */
    Map<String, String> generationCodes(Long tableId);

    /**
     * Get the table definition list that comes with the database
     *
     * @param dataSourceConfigId The configuration ID of the data source
     * @param name               table name
     * @param comment            Table description
     * @return table definition list
     */
    List<DatabaseTableRespVO> getDatabaseTableList(Long dataSourceConfigId, String name, String comment);

}
