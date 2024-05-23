package com.zscmp.main.app.mybatis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlarmMsg implements Serializable {
    private String uuid;
    private String cloudAccountId;
    private String name;
    private String level;
    private String content;
    private String readStatus;
    private String resourceId;
    private String resourceType;
    private String resourceName;
    private String expr;
    private String vdcId;
    private String cloudType;
    private Date alarmDate;
    private Date createDate;
    private Date updateDate;
}
