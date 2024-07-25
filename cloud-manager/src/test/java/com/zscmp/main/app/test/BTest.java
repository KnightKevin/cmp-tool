package com.zscmp.main.app.test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;

import com.zscmp.main.app.DomainUserReply;
import com.zscmp.main.app.PosixAccountUser;
import com.zscmp.main.app.PosixGroup;
import com.zscmp.main.app.TreeNode;
import com.zscmp.main.app.UserLdapRepository;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class BTest {

    @Autowired
    private UserLdapRepository userLdapRepository;

    private static Integer n=1;

    private static final String gid = "1720602843";

    @Autowired
    private LdapTemplate ldapTemplate;

    @Test
    public void aTest() throws IOException {

        // 先创建三层ou
        final String rootDn = "ou=xxx,ou=users";
        // userLdapRepository.createOu("a", "");
        userLdapRepository.createOu("a", "");

        userLdapRepository.createOu("b", "ou=a");

        userLdapRepository.createOu("c", "ou=b,ou=a");


        addUser("u1","1", "ou=a");
        addUser("u2", "2", "ou=b,ou=a");
        addUser("u3","3", "ou=c,ou=b,ou=a");

    }

    @Test
    public void bTest() throws IOException {

        //userLdapRepository.deleteOU("ou=cmp");
        

        List<TreeNode> list = buildTree(3);

        userLdapRepository.createOu("cmp", "");
        PosixGroup group = new PosixGroup();
        group.setCommonName("用户组");
        group.setGidNumber(gid);
        userLdapRepository.createGroup(group, "cmp");

        // createVdc(list, "ou=cmp");

    }

    @Test
    public void cTest() throws IOException {

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<String> ids= ldapTemplate.search(
                "ou=cmp",
                "(objectclass=*)",
                searchControls,
                (AttributesMapper<String>) attrs -> {
                    //return attrs.get("ou").get().toString();
                    boolean isOu = attrs.get("ou") != null;
                    
                    if (isOu) {
                        return (String)attrs.get("ou").get();
                    } else {
                        return (String)attrs.get("cn").get();
                    }

                }
        );


        log.info("gid");

    }

    @Test
    public void dTest() {
        LdapQuery query = LdapQueryBuilder.query()
                .base("ou=a")
                .filter("(objectClass=*)");

        List<PosixAccountUser> searchResults = ldapTemplate.search(query, new ContextMapper<PosixAccountUser>() {

            @Override
            public PosixAccountUser mapFromContext(Object ctx) throws NamingException {
                DirContextAdapter context = (DirContextAdapter)ctx;
                String dd = context.getNameInNamespace();
                String cc = context.getStringAttribute("cn");
                return null;
            }
            
        });


        // Collections.reverse(searchResults);

        // searchResults.forEach(i->{
        //     ldapTemplate.unbind(i);
        // });
    }

    @Test
    public void eTest() {
        
        Name oldDn = LdapNameBuilder.newInstance("cn=f2,ou=cmp")
                .build();

        Name newDn = LdapNameBuilder.newInstance(" ou=aaa,ou=cmp")
                .add("cn", "f2")
                .build();

        ldapTemplate.rename(oldDn, newDn);

    }

    private void createVdc(List<TreeNode> list, String parentDn) {
        for (TreeNode treeNode : list) {
            userLdapRepository.createOu(treeNode.getName(), parentDn);

            // 添加用户

            for (DomainUserReply i : treeNode.getUsers()) {
                PosixAccountUser user = new PosixAccountUser();
                user.setCn(i.getUsername());
                user.setSn("用户");
                user.setUid(i.getUsername());
                user.setUidNumber(2000+(n++)+"");
                user.setGidNumber(gid);
                user.setHomeDirectory("/share/home/"+i.getUsername());
                user.setUserPassword("password");
                userLdapRepository.create(user, parentDn);
            }
            createVdc(treeNode.getChildren(), "ou="+treeNode.getName()+","+parentDn);
        }

    }

    private void addUser(String uid, String uidNumber, String dnPath) {
        PosixAccountUser user = new PosixAccountUser();

        user.setCn("John Doe"+uid);
        user.setSn("sn");
        user.setUid(uid);
        user.setUidNumber(uidNumber);
        user.setGidNumber("5001");
        user.setUserPassword("password");
        user.setHomeDirectory("/share/"+uid);
        user.setLoginShell("/bin/bash");
        user.setUserPassword("password");

        // ou-用于组织目录  cn-表示具体的人
        userLdapRepository.create(user, dnPath);
    }

    private List<TreeNode> buildTree(int depth) {

        List<TreeNode> list = new ArrayList<>();

        list.add(buildDepthTreeNode("组织1", depth));
        list.add(buildDepthTreeNode("组织2", depth));
        list.add(buildDepthTreeNode("组织3", depth));

        return list;
    }

    private TreeNode buildTreeNode(String name) {

        TreeNode treeNode = new TreeNode();
        treeNode.setName(name);
        treeNode.setUuid(UUID.fastUUID().toString());

        treeNode.getUsers().add(buildDomainUserReply());

        return treeNode;
    }

    private TreeNode buildDepthTreeNode(String name, int depth) {
        TreeNode treeNode1 = buildTreeNode(name);

        TreeNode treeNode2 = treeNode1;
        for (int i=0;i<depth;i++) {
            treeNode1.getChildren().add(buildTreeNode(name+i));
            treeNode1 = treeNode1.getChildren().get(0);
        }

        return treeNode2;
    }

    private DomainUserReply buildDomainUserReply() {
        DomainUserReply reply = new DomainUserReply();
        reply.setUuid(UUID.randomUUID().toString());
        reply.setUsername("username"+(n++));
    
        return reply;
    }
}
