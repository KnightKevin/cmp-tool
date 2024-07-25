package com.zscmp.main.app.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.zscmp.main.app.App;
import com.zscmp.main.app.DbColumnToJavaType;
import com.zscmp.main.app.QFieldBuild;
import com.zscmp.main.app.ToCamelCaseMethod;
import com.zscmp.main.app.model.DbField;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = App.class)
public class GenerateCmpCodeTest {

    private final String baseDir = "target/gen/";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Configuration configuration;


    @Test
    public void genCode() throws Exception {

        final String tableName = "smg_sys_scan_task";

        final String className = toCamelCase(tableName);

        List<DbField> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String columnType = resultSet.getString("TYPE_NAME");
                list.add(new DbField(columnName, columnType));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // cmd bean中会排除数据库中那些不须要处理的字段
        String[] cmdExcludeFields = {"createDate", "updateDate"};


        Map<String, Object> model = new HashMap<>();
        // model.put("className", className);
        // model.put("tableName", tableName);
        // model.put("list", list);


        model.put("tableClassVarName", className);
        model.put("className", className);
        model.put("entityPackage", "com.package");
        model.put("module", "module");
        model.put("list", list);
        model.put("cmdExcludeFields", cmdExcludeFields);
        model.put("toCamelCaseMethod", new ToCamelCaseMethod());
        model.put("dbColumnToJavaType", new DbColumnToJavaType());
        model.put("qFieldBuild", new QFieldBuild());

        final String baseDir = "target/gen/";
        final String entityFilePath = baseDir+className+".java";
        final String replyFilePath = baseDir+className+"Reply.java";
        final String qFilePath = baseDir+String.format("Q%s.java", className);
        final String serviceFilePath = baseDir+String.format("I%sService.java", className);
        final String serviceImplFilePath = baseDir+String.format("%sServiceImpl.java", className);
        final String qryFilePath = baseDir+String.format("%sQry.java", className);



        deleteDir(baseDir);

        Files.createDirectories(Paths.get(baseDir));

        final String fltPrefix = "cmp/";
        genFile(fltPrefix+"entity.ftl", entityFilePath, model);

        genFile(fltPrefix+"reply.ftl", replyFilePath, model);

        genFile(fltPrefix+"q.ftl", qFilePath, model);

        genFile(fltPrefix+"service.ftl", serviceFilePath, model);

        genFile(fltPrefix+"serviceImpl.ftl", serviceImplFilePath, model);

        genFile(fltPrefix+"qry.ftl", qryFilePath, model);


    }

        public void writeStringToFile(String content, String filePath) throws IOException {




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


    public static String toCamelCase(String inputString) {
        StringBuilder camelCaseString = new StringBuilder();
        boolean nextCharToUpper = false;

        // 第一个字符大写
        if (inputString != null && inputString.length() > 0) {
            camelCaseString.append(Character.toUpperCase(inputString.charAt(0)));
        }

        // 从第二个字符开始处理
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (currentChar == '_') {
                nextCharToUpper = true;
            } else {
                if (nextCharToUpper) {
                    camelCaseString.append(Character.toUpperCase(currentChar));
                    nextCharToUpper = false;
                } else {
                    camelCaseString.append(currentChar);
                }
            }
        }

        return camelCaseString.toString();
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

    private void genFile(String ftl, String f, Map<String, Object> m) throws Exception {
        Template template = configuration.getTemplate(ftl);
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, m);
        writeStringToFile(s, f);
    }

}
