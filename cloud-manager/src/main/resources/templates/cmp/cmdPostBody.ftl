{
<#list list as i>
 "${i.varName}":"${i.varName}"<#if i?is_last><#else>,</#if>
</#list>
}