<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zscmp.main.app.mybatis.${className}Mapper">

    <!--批量新增-->
    <insert id="insertBatch" parameterType="java.util.List">
        insert into ${tableName} (<#list list as i>`${i.name}`<#if !i?is_last>,</#if></#list>)
        VALUES
        <foreach collection="list" item="item" separator=",">
        (<#list list as i><#noparse>#</#noparse>{item.${toCamelCaseMethod(i.name)}}<#if !i?is_last>,</#if></#list>)
        </foreach>
    </insert>
</mapper>