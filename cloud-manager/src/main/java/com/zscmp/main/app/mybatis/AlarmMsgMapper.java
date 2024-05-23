package com.zscmp.main.app.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AlarmMsgMapper extends BaseMapper<AlarmMsg> {
    void insertBatch(List<AlarmMsg> list);
}
