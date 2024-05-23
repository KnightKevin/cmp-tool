package com.zscmp.main.app.mybatis;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ${className}Mapper extends BaseMapper<${className}> {
    void insertBatch(List<${className}> list);
}
