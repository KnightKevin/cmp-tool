package com.zscmp.main.app;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author xmm
 */
@Getter
@Setter
public class DomainUserBinding {
    private String uuid;
    private String userId;
    private String username;
    private String roleId;
    private String roleName;
    private String roleKey;
    private String roleDesc;
    private String vdcId;
    private String vdcName;
    private Date createDate;
    private String createBy;

    public DomainUserBinding(String userId, String roleId, String vdcId) {
        this.userId = userId;
        this.roleId = roleId;
        this.vdcId = vdcId;
    }

    public DomainUserBinding() {
    }
}
