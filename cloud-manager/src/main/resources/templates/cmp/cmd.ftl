package com.zscmp.resource.model.req;

import java.util.Date;
import lombok.Data;

@Data
public class ${className}Cmd {
<#list list as i>

   <#if !(cmdExcludeFields?seq_contains(i.varName))>
    private ${dbTypeConvert(i.dataType)} ${i.varName};
   </#if>
</#list>
}
