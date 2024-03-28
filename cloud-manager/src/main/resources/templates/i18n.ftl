<#list map?keys as group>
<#list map[group]?keys as k>
${k} = ${map[group][k]}
</#list>


</#list>