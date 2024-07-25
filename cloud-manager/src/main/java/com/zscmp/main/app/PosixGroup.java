package com.zscmp.main.app;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

@Data
@Entry(objectClasses = {"top", "posixGroup"})
public class PosixGroup {

    @Id
    private Name id;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "gidNumber")
    private String gidNumber;
}
