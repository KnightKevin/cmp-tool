// js
<#list list as i>
{name:'${i.name}', url:'/actuator/health'},
</#list>

<#list list as i>
location /api/${i.name} {
    proxy_pass <#noparse>${cmpHost}</#noparse>:${i.port};
    rewrite ^/api/${i.name}/(.*)$ /$1 break;
}

</#list>