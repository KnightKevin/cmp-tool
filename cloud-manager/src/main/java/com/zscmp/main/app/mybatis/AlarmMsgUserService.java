package com.zscmp.main.app.mybatis;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AlarmMsgUserService extends ServiceImpl<AlarmMsgUserMapper, AlarmMsgUser> implements IService<AlarmMsgUser> {
}

