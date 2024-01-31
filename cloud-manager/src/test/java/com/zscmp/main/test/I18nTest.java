package com.zscmp.main.test;

import com.zscmp.common.annotation.ActionKey;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class I18nTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void myTest() {
        String packageName = "com.zscmp"; // 替换成你的包名
        List<Class<?>> classes = getClassesWithAnnotation(packageName, RestController.class);


        Map<String, String> actions = new HashMap<>();
        // 处理扫描到的类
        for (Class<?> clazz : classes) {
            // 获取到所有有ActionKey的方法
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(ActionKey.class)) {
                    ActionKey actionKey = method.getAnnotation(ActionKey.class);
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    actions.put(actionKey.code(), apiOperation.value());
                }
            }

        }

        actions.forEach((k,v)->{
            System.out.println(String.format("action.%s=%s", k, v));
        });
    }

    public List<Class<?>> getClassesWithAnnotation(String basePackage, Class annotation) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));

        List<Class<?>> classes = new ArrayList<>();
        for (org.springframework.beans.factory.config.BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }
}
