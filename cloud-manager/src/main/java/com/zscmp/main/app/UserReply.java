package com.zscmp.main.app;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserReply {
    private String uuid;
    private String username;
    private String realName;
    private String nickName;
    private String avatar;
    private String email;
    private String phone;
    // @ApiModelProperty(value = "简介")
    private String description;
    // @ApiModelProperty(value = "首次登录强制修改密码")
    private boolean mustModifyPwd = true;
    // @ApiModelProperty(value = "是否定期修改密码，为true则regularlyModifyPwdDays有效")
    private Boolean regularlyModifyPwd = false;
    // @ApiModelProperty(value = "regularlyModifyPwd=true时配置的天数")
    private Integer regularlyModifyPwdDays = 0;
    // @ApiModelProperty(value = "是否可用")
    private boolean available = true;
    // @ApiModelProperty(value = "创建人id")
    private String createBy;
    // @ApiModelProperty(value = "创建人")
    private String createByName;
    // @ApiModelProperty(value = "创建日期")
    private Date createDate;
    // @ApiModelProperty(value = "更新日期")
    private Date updateDate;
    // @ApiModelProperty(value = "认证类型")
    private String type;
    // @ApiModelProperty(value = "是否为登录用户")
    private Boolean isLoginUser;
}
