package com.zscmp.main.app.mybatis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ${className} implements Serializable {
    <#list list as i>
    private ${dbColumnToJavaType(i.type)} ${toCamelCaseMethod(i.name)};
    </#list>
}
