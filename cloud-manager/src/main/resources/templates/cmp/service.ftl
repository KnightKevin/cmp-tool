package ${module}.service;

import com.querydsl.core.QueryResults;
import ${entityPackage}.${className};
import ${module}.req.${className}Qry;

public interface I${className}Service {
    QueryResults<${className}> pageList(${className}Qry qry);

    ${className} getByUuid(String uuid);

    ${className} create(${className} entity);

    ${className} update(${className} entity);

    Long delete(String[] uuids);

}