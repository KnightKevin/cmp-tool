package com.zscmp.main.app.mybatis;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface AlarmMsgUserMapper extends BaseMapper<AlarmMsgUser> {
    void insertBatch(List<AlarmMsgUser> list);
}
