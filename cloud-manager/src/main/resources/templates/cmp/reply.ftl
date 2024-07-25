package com.zscmp.resource.model;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Reply {
<#list list as i>
    private ${dbColumnToJavaType(i.type)} ${toCamelCaseMethod(i.name)};
</#list>

}
