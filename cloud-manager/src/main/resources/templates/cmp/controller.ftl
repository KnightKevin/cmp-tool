package com.zscmp.bff.resource.controller;

import com.zscmp.common.annotation.ActionKey;
import com.zscmp.common.enums.RespCode;
import com.zscmp.common.model.PageResults;
import com.zscmp.common.response.RestBean;
import com.zscmp.infrastructure.utils.CheckUtil;
import com.zscmp.infrastructure.utils.RestUtil;
import com.zscmp.infrastructure.utils.UserUtil;
import ${module}.api.I${className}Api;
import ${module}.model.reply.${className}Reply;
import ${module}.model.req.${className}Cmd;
import ${module}.model.req.${className}Qry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "[vdc][${className}}]")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/${tableClassVarName}")
public class ${className}Controller {

    @Autowired
    private I${className}Api ${tableClassVarName}Api;

    @ApiOperation(value = "分页查询")
    @PreAuthorize("hasAuthority('${tableClassVarName}PageList')")
    @GetMapping("/PageList")
    public RestBean<PageResults<${className}Reply>> pageList(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "offset", defaultValue = "0") Long offset,
            @RequestParam(value = "limit", defaultValue = "10") Long limit,
            @RequestParam(value = "order", required = false, defaultValue = "createDate") String order,
            @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort
    ) {
        CheckUtil.check(limit > 1000, RespCode.CHK_LIMIT, 1000);

        ${className}Qry qry = new ${className}Qry(offset, limit, order, sort);
        qry.setUuid(uuid);
        qry.setName(name);

        return RestUtil.success(${tableClassVarName}Api.pageList(qry));
    }


    @ApiOperation(value = "查询详情")
    @PreAuthorize("hasAuthority('${tableClassVarName}Detail')")
    @GetMapping("/QueryDetail")
    public RestBean<${className}Reply> queryDetail(
            @RequestParam(value = "uuid") String uuid
    ) {
        return RestUtil.success(${tableClassVarName}Api.getByUuid(uuid));
    }

    
    @ApiOperation(value = "新增")
    @PreAuthorize("hasAuthority('${tableClassVarName}Add')")
    @ActionKey(code = "${tableClassVarName}Add")
    @PostMapping("/Create")
    public RestBean<${className}Reply> create(
            @RequestBody @Validated ${className}Cmd cmd
    ) {
        cmd.setCreateBy(UserUtil.getUserId());
        cmd.setCreateByName(UserUtil.getUserName());
        return RestUtil.success(${tableClassVarName}Api.create(cmd));
    }

    @ApiOperation(value = "更新")
    @PreAuthorize("hasAuthority('${tableClassVarName}Update')")
    @ActionKey(code = "${tableClassVarName}Update")
    @PostMapping("/Update")
    public RestBean<${className}Reply> update(
            @RequestBody @Validated ${className}Cmd cmd
    ) {
        return RestUtil.success(${tableClassVarName}Api.update(cmd));
    }

    @ApiOperation(value = "删除")
    @PreAuthorize("hasAuthority('${tableClassVarName}Delete')")
    @ActionKey(code = "${tableClassVarName}Delete")
    @PostMapping("/Delete")
    public RestBean<?> delete(
            @RequestBody String[] uuids
    ) {
        ${tableClassVarName}Api.delete(uuids);
        return RestUtil.success();
    }

    
}
