package ${module}.api;

import ${module}.model.reply.${className}Reply;
import ${module}.model.req.${className}Cmd;
import ${module}.model.req.${className}Qry;

import com.zscmp.common.model.PageResults;

public interface I${className}Api {
    PageResults<${className}Reply> pageList(${className}Qry qry);

    ${className}Reply getByUuid(String uuid);

    ${className}Reply create(${className}Cmd cmd);

    ${className}Reply update(${className}Cmd cmd);

    void delete(String[] uuids);

}