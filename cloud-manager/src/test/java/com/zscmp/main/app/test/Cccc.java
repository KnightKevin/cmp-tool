package com.zscmp.main.app.test;


import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.RowFilter.Entry;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.serializer.Deserializer;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.Data;
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



        Ddd d = json.toJavaObject(Ddd.class);


        Ddd cc = JSONObject.parseObject(jsonString, Ddd.class);


        log.info("asdfasdf");

        


    }

    @Data
    public static class Ddd {
        @JSONField(name = "name")
        private String a;

        @JSONField(name = "address", parseFeatures = {Feature.})
        private String b = "";
    }


    public static class Cccc implements ObjectDeserializer {

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String v = parser.parseObject(String.class);

            return (T)(v == null ? "" : v);
        }

        @Override
        public int getFastMatchToken() {
            return 0;
        }
        
    }

}