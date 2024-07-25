package com.zscmp.main.app;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogRequestAspect {

    @Pointcut("@annotation(LogRequest)")
    public void logRequestPointcut() {}

    @AfterReturning(pointcut = "logRequestPointcut()")
    public void logRequest(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> logInfo = new HashMap<>();

        // 获取方法参数注解
        for (int i = 0; i < method.getParameterAnnotations().length; i++) {
            for (int j = 0; j < method.getParameterAnnotations()[i].length; j++) {
                if (method.getParameterAnnotations()[i][j] instanceof RequestBody) {
                    Object requestBody = args[i];
                    if (requestBody != null) {
                        desensitizeObject(requestBody, logInfo);
                    }
                }
            }
        }

        String methodName = method.getName();
        System.out.println("Request to " + methodName + ": " + logInfo);
    }

    private void desensitizeObject(Object object, Map<String, Object> logInfo) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (field.isAnnotationPresent(Sensitive.class)) {
                    logInfo.put(field.getName(), "******");  // 脱敏处理
                } else {
                    logInfo.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

