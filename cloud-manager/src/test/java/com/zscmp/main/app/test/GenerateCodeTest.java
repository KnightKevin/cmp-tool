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
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.zscmp.main.app.DbColumnToJavaType;
import com.zscmp.main.app.ToCamelCaseMethod;
import com.zscmp.main.app.model.DbField;
import com.zscmp.main.app.mybatis.AlarmMsg;
import com.zscmp.main.app.mybatis.AlarmMsgMapper;
import com.zscmp.main.app.mybatis.AlarmMsgUser;
import com.zscmp.main.app.mybatis.AlarmMsgUserMapper;

import cn.hutool.core.date.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class GenerateCodeTest {

    private final String baseDir = "target/gen/";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Configuration configuration;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AlarmMsgMapper alarmMsgMapper;

    @Autowired
    private AlarmMsgUserMapper alarmMsgUserMapper;

    @Test
    public void test() {


        // 每天一万数据
        int num = 10000;

        // 须要多少天数据
        int day = 60;


        Calendar today = Calendar.getInstance();

        for (int i = 0 ; i<day ;i++) {
            if (i != 0) {
                today.add(Calendar.DATE, -1);
            }
            Date date = today.getTime();

            saveData(num, date);

        }

    }

    private void saveData(int num, Date date) {
        long s = System.currentTimeMillis();

        List<AlarmMsg> alarmMsgList = new ArrayList<>(num);
        List<AlarmMsgUser> users = new ArrayList<>(num);
        for (int i =0;i<num;i++) {
            String id = UUID.randomUUID().toString().replace("-", "");
            AlarmMsg msg = new AlarmMsg();
            msg.setUuid(id);
            msg.setCloudAccountId("a5e1e8bbea7842578661fcce2dfca086");
            msg.setCloudType("ZSTACK");
            msg.setName("云主机CPU利用率>=90%");
            msg.setLevel("1");
            msg.setContent("{\"forDuration\":5,\"forDurationUnit\":\"m\",\"metricAndCondition\":\"CPU Utilization >= 90.00 %\"}");
            msg.setReadStatus("unread");
            msg.setResourceId("ff8080818f13417f018f134369560006");
            msg.setResourceType("vm");
            msg.setResourceName("直接创建asdf");
            msg.setExpr("");
            msg.setVdcId("");
            msg.setAlarmDate(date);
            msg.setCreateDate(date);
            msg.setUpdateDate(date);

            alarmMsgList.add(msg);

            String uuid = UUID.randomUUID().toString().replace("-", "");
            AlarmMsgUser user = new AlarmMsgUser();
            user.setUuid(uuid);
            user.setAlarmId(id);
            user.setUserId("37a2ffa6d1664987bca5c814801430bb");
            user.setReadStatus("unread");
            user.setCreateDate(date);
            users.add(user);

        }

        alarmMsgMapper.insertBatch(alarmMsgList);
        alarmMsgUserMapper.insertBatch(users);

        log.info("{} time： {}s", DateUtil.formatDate(date), (System.currentTimeMillis() - s)/1000);

    }

    @Test
    public void genCode() throws Exception {

        String t = """
                {
                    asdfasdfasdfasdf
                }
                """;
        
        log.info("dadsdfas {}", t);
        final String tableName = "alarm_msg_user";

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


        Map<String, Object> model = new HashMap<>();
        model.put("className", className);
        model.put("tableName", tableName);
        model.put("list", list);
        model.put("toCamelCaseMethod", new ToCamelCaseMethod());
        model.put("dbColumnToJavaType", new DbColumnToJavaType());


        final String baseDir = "target/gen/";
        final String entityFilePath = baseDir+className+".java";
        final String mapperFilePath = baseDir+String.format("%sMapper.java", className);
        final String serviceFilePath = baseDir+String.format("%sService.java", className);
        final String xmlFilePath = baseDir+String.format("%sMapper.xml", className);


        deleteDir(baseDir);

        Files.createDirectories(Paths.get(baseDir));

        final String fltPrefix = "mybatis/";
        genFile(fltPrefix+"entity.ftl", entityFilePath, model);

        genFile(fltPrefix+"mapper.ftl", mapperFilePath, model);

        genFile(fltPrefix+"service.ftl", serviceFilePath, model);

        genFile(fltPrefix+"xml.ftl", xmlFilePath, model);


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
