package com.zscmp.main.app;

import java.lang.reflect.Field;

import com.alibaba.fastjson.serializer.ValueFilter;

public class PasswordValueFilter implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            if (field.isAnnotationPresent(Sensitive.class)) {
                return "******";  // 用星号替换敏感信息
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return value;
    }
}
