package com.zscmp.main.test;

//import com.zscmp.common.annotation.ActionKey;
//import io.swagger.annotations.ApiOperation;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.zscmp.cloud.manager.JavaFileVisitor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


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
    public void stringLiteralI18nTest() {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool\\maintenance-server"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        try {
            Files.walkFileTree(startPath, visitor);
            List<Path> javaFiles = visitor.getJavaFiles();

            for (Path javaFile : javaFiles) {

                String javaClassName = javaFile.getFileName().toString().replace(".java", "");

                if (!javaFile.toUri().toString().endsWith(".java")) {
                    continue;
                }

                CompilationUnit cu = StaticJavaParser.parse(javaFile);

                cu.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(StringLiteralExpr n, Void arg) {
                        super.visit(n, arg);

                        if (n.getValue().contains("CheckUtil.check")) {
                            log.info("已使用, file {} at line {}", javaClassName,n.getRange().map(r -> r.begin.line).orElse(-1));
                        }

                    }
                }, null);

            }


        } catch (IOException e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }
    }

    @Test
    public void checkUtilFunctionUseInfo() throws Exception {
        Path startPath = Paths.get("D:\\work_space\\cmp-tool\\maintenance-server"); // 指定你要遍历的目录路径
        JavaFileVisitor visitor = new JavaFileVisitor();

        final String targetMethodCall = "CheckUtil#check";

        Files.walkFileTree(startPath, visitor);
        List<Path> javaFiles = visitor.getJavaFiles();

        List<String> params = new ArrayList<>();

        for (Path javaFile : javaFiles) {

            if (!javaFile.toUri().toString().endsWith(".java")) {
                continue;
            }

            CompilationUnit cu = StaticJavaParser.parse(javaFile);

            cu.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr method, Void arg) {
                    super.visit(method, arg);

                    Expression scope = method.getScope().orElse(null);
                    String methCall = String.format("%s#%s", scope==null?"":scope.toString(),method.getName().getIdentifier());
                    List<Expression> arguments = method.getArguments();
                    if (methCall.equalsIgnoreCase(targetMethodCall)) {

                        if (arguments.size() >= 3 && arguments.get(2).isStringLiteralExpr()) {

                            String v = arguments.get(2).asStringLiteralExpr().asString();
                            params.add(v);
                        }

                    }

                }
            }, null);

        }
    }

    /**
     * 处理国际化文件,过滤掉不用的key
     * */
    @Test
    public void filterI18nFile() throws Exception {

        final String modulePath = "D:\\work_space\\cmp-tool\\vm-server\\";
        final String i18nFile = modulePath+"starter\\src\\main\\resources\\i18n\\Messages_zh_CN.properties";
        final String respCodeFile = modulePath+"\\provider\\src\\main\\java\\com\\zscmp\\vm\\enums\\RespCode.java";
        Properties properties =loadPropertiesFile(i18nFile);

        Map<String, Map<String, String>> map = new HashMap<>();

        List<String> ignores = Arrays.asList("action", "preset", "exception");

        List<String> validateStringList = getCheckUtilLiteral(modulePath);

        Map<String, String> respCodeStringMap = getRespCode(respCodeFile);


        final String validatePrefix = "validate";
        final String exceptionPrefix = "exception";


        for (String k : properties.stringPropertyNames()) {

            if (!k.contains(".")) {
                throw new RuntimeException("key必须有‘.’");
            }

            String group = k.split("\\.")[0];

            if (ignores.contains(group)) {
                continue;
            }

            if (!map.containsKey(group)) {
                map.put(group, new HashMap<>());
            }

            map.get(group).put(k, properties.getProperty(k));
        }

        // 添加resp code数据
        Map<String, String> respCodeI18nMap = new HashMap<>();
        respCodeStringMap.forEach((k,v)->{
            respCodeI18nMap.put(exceptionPrefix+"."+k, v);
        });
        map.put(exceptionPrefix, respCodeI18nMap);

        // 开始过滤
        map.forEach((group, items)->{
            Map<String, String> afterItems = new HashMap<>();
            if (group.equalsIgnoreCase(validatePrefix)) {
                items.forEach((k,v)->{
                    String i18nKey = k.replace(validatePrefix+".", "");
                    if (validateStringList.contains(i18nKey)) {
                        afterItems.put(k, v);
                    }
                });

                items.clear();
                items.putAll(afterItems);
            }
        });

        Map<String, Object> model = new HashMap<>();
        model.put("map", map);


        final String baseDir = "target/gen/";
        final String filePath = baseDir+"Messages_en_CN.properties";

        deleteDir(baseDir);

        Files.createDirectories(Paths.get(baseDir));

        genFile("i18n.ftl", filePath, model);

    }

    private List<String> getCheckUtilLiteral(String dir) throws Exception {
        Path startPath = Paths.get(dir); // 指定你要遍历的目录路径

        JavaFileVisitor visitor = new JavaFileVisitor();

        final String targetMethodCall = "CheckUtil#check";

        Files.walkFileTree(startPath, visitor);
        List<Path> javaFiles = visitor.getJavaFiles();

        List<String> params = new ArrayList<>();

        for (Path javaFile : javaFiles) {

            if (!javaFile.toUri().toString().endsWith(".java")) {
                continue;
            }

            CompilationUnit cu = StaticJavaParser.parse(javaFile);

            cu.accept(new VoidVisitorAdapter<Void>() {
                @Override
                public void visit(MethodCallExpr method, Void arg) {
                    super.visit(method, arg);

                    Expression scope = method.getScope().orElse(null);
                    String methCall = String.format("%s#%s", scope==null?"":scope.toString(),method.getName().getIdentifier());
                    List<Expression> arguments = method.getArguments();
                    if (methCall.equalsIgnoreCase(targetMethodCall)) {

                        if (arguments.size() >= 3 && arguments.get(2).isStringLiteralExpr()) {

                            String v = arguments.get(2).asStringLiteralExpr().asString();
                            params.add(v);
                        }

                    }

                }
            }, null);

        }

        return params;
    }

    private Map<String, String> getRespCode(String filename) throws Exception {

        Path filePath = new File(filename).toPath();

        CompilationUnit cu = StaticJavaParser.parse(filePath);

        String enumName = "RespCode";

        EnumDeclaration declaration = cu.getEnumByName(enumName).orElse(null);

        // 文件可能全部被注释了
        if (declaration == null) {
            throw new RuntimeException("error");
        }


        Map<String, String> map = new HashMap<>();
        declaration.getEntries().forEach(i->{
            map.put(i.getArguments().get(0).toString(), i.getArguments().get(1).toString());
        });

        return map;
    }


    @Test
    public void respCodeI18n() throws IOException {
        String filename = "D:\\work_space\\cmp-tool\\maintenance-server\\provider\\src\\main\\java\\com\\zscmp\\maintenance\\enums\\RespCode.java";

        Path filePath = new File(filename).toPath();

        CompilationUnit cu = StaticJavaParser.parse(filePath);

        String enumName = "RespCode";

        EnumDeclaration declaration = cu.getEnumByName(enumName).orElse(null);

        // 文件可能全部被注释了
        if (declaration == null) {
            throw new RuntimeException("error");
        }


        declaration.getEntries().forEach(i->{
            System.out.printf("exception.%s = %s%n", i.getArguments().get(0).toString(), i.getArguments().get(1).toString().replace("\"", ""));
        });
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

    public Properties loadPropertiesFile(String filename) throws IOException {
        Properties properties = new Properties();
        // 使用ClassLoader获取资源的路径，以便从相对于classpath的位置加载
        // 加载文件
        File file = new File(filename);
        properties.load(Files.newInputStream(file.toPath()));
        return properties;
    }

    private void genFile(String ftl, String f, Map<String, Object> m) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        String resourcePath = getClass().getClassLoader().getResource("templates").getPath();
        cfg.setDirectoryForTemplateLoading(new File(resourcePath));
        Template template = cfg.getTemplate(ftl);
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, m);
        writeStringToFile(s, f);
    }

    private void deleteDir(String baseDir) throws Exception {
        Path folderPath = Paths.get(baseDir);

        if (Files.notExists(folderPath)) {
            return;
        }

        Files.walk(folderPath)
                .sorted(Comparator.reverseOrder())
                .forEach(path->{
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void writeStringToFile(String content, String filePath) throws IOException {




        // 使用 Paths 工具类获取文件路径
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
        Files.createFile(path);


        // 使用 Files 类写入文件
        // 这里使用了 StandardOpenOption.CREATE 和 StandardOpenOption.APPEND
        // CREATE - 如果文件不存在，则创建；如果文件存在，不做任何事情
        // APPEND - 如果文件存在，写入的内容会追加到文件末尾；否则，和 CREATE 一样创建新文件
        // 如果想要每次写入时覆盖文件，可以不使用 StandardOpenOption.APPEND
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

}
