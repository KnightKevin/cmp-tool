package com.zscmp.resource.model;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Reply {
<#list list as i>
    private ${dbTypeConvert(i.dataType)} ${i.varName};
</#list>

}
