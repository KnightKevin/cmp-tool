package com.zscmp.main.app;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Data;

@Data
@Entry(objectClasses = { "top", "posixAccount","person","customPerson", "inetOrgPerson" })
public class PosixAccountUser {
    @Id
    private String uid;

    @Attribute(name = "cn")
    private String cn;

    @Attribute(name = "sn")
    private String sn;

    @Attribute(name = "uidNumber")
    private String uidNumber;

    @Attribute(name = "gidNumber")
    private String gidNumber;

    @Attribute(name = "homeDirectory")
    private String homeDirectory;

    @Attribute(name = "loginShell")
    private String loginShell = "/bin/bash";

    @Attribute(name = "userPassword")
    private String userPassword;

    @Attribute(name = "gecos")
    private String gecos;

    @Attribute(name = "seeAlso")
    private String seeAlso = "";

    @Attribute(name = "telephoneNumber")
    private String telephoneNumber;


}
