package com.zscmp.main.app;

import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.NameClassPairCallbackHandler;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


@Repository
public class UserLdapRepository {

    @Autowired
    private LdapTemplate ldapTemplate;



    public void create(PosixAccountUser user, String dnPath) {
        Name dn = LdapNameBuilder.newInstance(dnPath)
                .add("cn", user.getCn())
                .build();


        ldapTemplate.bind(dn, null, buildAttributes(user));
    }


    public void createGroup(PosixGroup group, String dnPath) {
        Name dn = LdapNameBuilder.newInstance()
        .add("ou", dnPath)
        .add("cn", group.getCommonName())
        .build();
        group.setId(dn);

        ldapTemplate.create(group);
    }



    public void createOu(String name, String parentDn) {
        Name dn = LdapNameBuilder.newInstance(parentDn)
                .add("ou", name)
                .build();
        Attributes attributes = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("organizationalUnit");
        attributes.put(ocattr);
        attributes.put("ou", name);
        ldapTemplate.bind(dn, null, attributes);
        System.out.println("Created OU: " + dn.toString());

    }

    


    // 递归删除OU及其所有子节点
    public void deleteOU(String ou) {
        LdapName ldapName = LdapUtils.newLdapName(ou);
        deleteSubtree(ldapName);
    }

    private void deleteSubtree(LdapName ldapName) {
            // 设置搜索控制器，用于递归搜索所有子节点
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // 执行递归搜索，获取所有子节点
            ldapTemplate.search(
                    ldapName, "(objectclass=*)", searchControls,
                    (NameClassPairCallbackHandler) ctx -> {
                        ldapTemplate.unbind(ctx.getNameInNamespace());
            });

            // 最后删除当前节点
            ldapTemplate.unbind(ldapName);

        }

    private Attributes buildAttributes(PosixAccountUser user) {
        Attributes attributes = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("top");
        ocattr.add("posixAccount");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");


        attributes.put(ocattr);
        attributes.put("cn", user.getCn());
        attributes.put("sn", user.getSn());
        attributes.put("uid", user.getUid());
        attributes.put("uidNumber", user.getUidNumber());
        attributes.put("gidNumber", user.getGidNumber());
        attributes.put("homeDirectory", user.getHomeDirectory());
        attributes.put("loginShell", user.getLoginShell());
        attributes.put("userPassword", user.getUserPassword());


        return attributes;
    }
}