package com.zscmp.main.app.mybatis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlarmMsgUser implements Serializable {
    private String uuid;
    private String userId;
    private String alarmId;
    private String readStatus;
    private Date createDate;
}
