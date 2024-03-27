package com.zscmp.main.test;

//import com.zscmp.common.annotation.ActionKey;
//import io.swagger.annotations.ApiOperation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.zscmp.cloud.manager.JavaFileVisitor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class I18nTest {

    private static final String ActionKey = "ActionKey";

    private static final String ApiOperation = "ApiOperation";
    private static final String ACTION_KEY_CODE = "code";

    @Autowired
    private MessageSource messageSource;

    @Test
    public void actionI18nTest() {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        Map<String, String> actionMap = new HashMap<>();

        List<String> nonstandardMethod = new ArrayList<>();

        try {
            Files.walkFileTree(startPath, visitor);
            List<Path> javaFiles = visitor.getJavaFiles();


            // 操作条目数
            AtomicInteger n = new AtomicInteger();

            for (Path javaFile : javaFiles) {

                String javaClassName = javaFile.getFileName().toString().replace(".java", "");

                if (!javaFile.getFileName().toString().contains("Controller")) {
                    continue;
                }

                CompilationUnit cu = StaticJavaParser.parse(javaFile);

                ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(javaClassName).orElse(null);

                // 文件可能全部被注释了
                if (classDeclaration == null) {
                    continue;
                }

                String fullClassName = classDeclaration.getFullyQualifiedName().get();

                cu.findAll(MethodDeclaration.class).stream()
                        .filter(NodeWithPublicModifier::isPublic)
                        .forEach(m->{

                            String code = "";

                            String value = "";

                            if (!m.isAnnotationPresent(ActionKey)) {
                                return;
                            }
                            n.getAndIncrement();
                            Optional<AnnotationExpr> optional = m.getAnnotationByName(ActionKey);

                            AnnotationExpr annotationExpr = optional.get();
                            code = getAnnotationValueByName(ACTION_KEY_CODE, annotationExpr);

                            if (m.isAnnotationPresent(ApiOperation)) {

                                optional = m.getAnnotationByName(ApiOperation);

                                annotationExpr = optional.get();
                                value = getAnnotationValueByName("value", annotationExpr);

                            } else {
                                nonstandardMethod.add(fullClassName+"#"+m.getNameAsString());
                            }

                            actionMap.put(code, value);
                            log.info("{}={}", code, value);
                });
            }

            log.info("总条目数：{}", actionMap.size());
            log.info("不规范的方法定义条目：{}", nonstandardMethod);

        } catch (IOException e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }
    }

    @Test
    public void taskI18nTest() {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        Map<String, String> actionMap = new HashMap<>();

        List<String> nonstandardMethod = new ArrayList<>();

        try {
            Files.walkFileTree(startPath, visitor);
            List<Path> javaFiles = visitor.getJavaFiles();

            for (Path javaFile : javaFiles) {

                String javaClassName = javaFile.getFileName().toString().replace(".java", "");

                if (!javaFile.getFileName().toString().contains("TaskKey")) {
                    continue;
                }

                CompilationUnit cu = StaticJavaParser.parse(javaFile);

                EnumDeclaration declaration = cu.getEnumByName(javaClassName).orElse(null);

                // 文件可能全部被注释了
                if (declaration == null) {
                    continue;
                }

                String fullClassName = declaration.getFullyQualifiedName().get();

                declaration.getEntries().forEach(i->{
                    log.info("{} = {}", i.getName(), i.getArguments().get(0).asStringLiteralExpr().asString());
                });
            }

            log.info("总条目数：{}", actionMap.size());
            log.info("不规范的方法定义条目：{}", nonstandardMethod);

        } catch (IOException e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }
    }

    @Test
    public void stepI18nTest() {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        Map<String, String> actionMap = new HashMap<>();

        List<String> nonstandardMethod = new ArrayList<>();

        try {
            Files.walkFileTree(startPath, visitor);
            List<Path> javaFiles = visitor.getJavaFiles();

            for (Path javaFile : javaFiles) {

                String javaClassName = javaFile.getFileName().toString().replace(".java", "");

                if (!javaFile.toUri().toString().endsWith("batch/StepKey.java")) {
                    continue;
                }

                CompilationUnit cu = StaticJavaParser.parse(javaFile);

                EnumDeclaration declaration = cu.getEnumByName(javaClassName).orElse(null);

                // 文件可能全部被注释了
                if (declaration == null) {
                    continue;
                }


                declaration.getEntries().forEach(i->{
                    log.info("{} = {}", i.getName(), i.getArguments().get(0).asStringLiteralExpr().asString());
                });
            }

            log.info("总条目数：{}", actionMap.size());
            log.info("不规范的方法定义条目：{}", nonstandardMethod);

        } catch (IOException e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }
    }

    @Test
    public void dd() {
        Locale locale = Locale.forLanguageTag("zh-CN");


        assertEquals(locale, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 检查一个具有actionKey注解的方法是否符合
     * */
    @Test
    public void checkActionKeyValidate() {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        List<String> list = new ArrayList<>();

        try {
            Files.walkFileTree(startPath, visitor);
            List<Path> javaFiles = visitor.getJavaFiles();


            // 操作条目数
            AtomicInteger n = new AtomicInteger();

            for (Path javaFile : javaFiles) {
                CompilationUnit cu = StaticJavaParser.parse(javaFile);

                cu.findAll(MethodDeclaration.class).stream()
                        .filter(NodeWithPublicModifier::isPublic)
                        .forEach(m->{

                            if (!m.isAnnotationPresent(ActionKey)) {
                                return;
                            }

                            // 获取注解锁设置的字段值
                            Optional<AnnotationExpr> optional = m.getAnnotationByName(ActionKey);
                            AnnotationExpr annotationExpr = optional.get();
                            String code = getAnnotationValueByName(ACTION_KEY_CODE, annotationExpr);

                            String fileInfo = String.format(javaFile.getFileName().toString()+"#"+m.getNameAsString());

                            if(!StringUtils.hasText(code)) {
                                list.add(fileInfo+"ActionKey code不可以为空");

                            }

                            if (!m.isAnnotationPresent(ApiOperation)) {
                                n.getAndIncrement();
                                list.add(fileInfo+"缺少对应的ApiOperation注解");
                            }

                        });
            }

            log.info("总条目数：{}", n);
            log.info("以下类的方法缺少@ApiOperation注解:{}", list);

        } catch (IOException e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }
    }


    private String getAnnotationValueByName(String fieldName, AnnotationExpr annotationExpr) {

        if (annotationExpr.isSingleMemberAnnotationExpr()) {
            return annotationExpr.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().getValue();
        } else {
            for (MemberValuePair pair : annotationExpr.asNormalAnnotationExpr().getPairs()) {

                if (pair.getNameAsString().equals(fieldName)) {
                    return pair.getValue().asStringLiteralExpr().getValue();
                }
            }
        }

        return "";
    }
}
