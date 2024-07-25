package com.zscmp.main.app;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmm
 */
@Getter
@Setter
public class TreeNode {
    private String uuid;
    private String parentUuid;
    private String name;
    private List<DomainUserReply> users = new ArrayList<>();
    private List<TreeNode> children = new ArrayList<>();
}
