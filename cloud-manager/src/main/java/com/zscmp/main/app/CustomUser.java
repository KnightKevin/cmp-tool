package com.zscmp.main.app;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

import javax.naming.Name;

@Data
@Entry(base = "dc=example,dc=com", objectClasses = "inetOrgPerson")
public class CustomUser {
    @Id
    private Name id;

    @Attribute(name = "cn")
    private String commonName;

    // 自定义属性
    @Attribute(name = "customAttribute")
    private String customAttributeValue;
}
