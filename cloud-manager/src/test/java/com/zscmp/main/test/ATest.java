package com.zscmp.main.test;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.RowFilter.Entry;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ATest {

    @Test
    public void aTest() throws IOException {
        // 读取JSON文件
        ClassPathResource resource = new ClassPathResource("json.json");
        byte[] jsonBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);

        JSONObject json = JSONObject.parseObject(jsonString);

        for (Map.Entry<String, Object> entry: json.entrySet()) {
            String k = entry.getKey();
            String v = (String) entry.getValue();

            if (Pattern.compile("[a-zA-Z]").matcher(v).find()) {
                if (v.toLowerCase().contains("rds")) {
                    log.info("k='{}' v= {}", k, v);
                }
            }
        }


    }

}