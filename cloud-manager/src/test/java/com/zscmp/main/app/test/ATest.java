package com.zscmp.main.app.test;

import static org.mockito.ArgumentMatchers.longThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.RowFilter.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.XStream;
import com.zscmp.main.app.PasswordValueFilter;
import com.zscmp.main.app.Sensitive;
import com.zscmp.main.app.test.ATest.Dd;

import lombok.AllArgsConstructor;
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

        Date ddd = json.getDate("dd");

        log.info("asdfasdf");

    }

    @Test
    public void bTest() throws Exception {

        String dd = "dddddd";
        try {
            JSONObject d = JSON.parseObject(dd);
        } catch(Exception e) {
            log.info("asdf");
        }

        AO o = new AO("aaa", "bbb");

        log.info("json : {}", JSONObject.toJSONString(o, new PasswordValueFilter()));
    }

    @Test
    public void cTest() throws Exception {

        Dd config = new Dd();
        config.setName("asdf");
        config.setAge(112);
        config.setList(Arrays.asList("a","b"));

        try {

            JAXBContext context = JAXBContext.newInstance(Dd.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(config, writer);

            String xmlString = writer.toString();
            System.out.println(xmlString);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dTest() throws Exception {
        String dd = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + //
                "<LifecycleConfiguration>\r\n" + //
                "    <Rule>\r\n" + //
                "        <Expiration>\r\n" + //
                "            <Days>30</Days>\r\n" + //
                "        </Expiration>\r\n" + //
                "        <ID>aslkdfj</ID>\r\n" + //
                "        <Prefix>ddd</Prefix>\r\n" + //
                "        <Status>Enabled</Status>\r\n" + //
                "    </Rule>\r\n" + //
                "    <Rule>\r\n" + //
                "        <Expiration>\r\n" + //
                "            <Days>30</Days>\r\n" + //
                "        </Expiration>\r\n" + //
                "        <ID>aslkdfj</ID>\r\n" + //
                "        <Prefix>ddd</Prefix>\r\n" + //
                "        <Status>Enabled</Status>\r\n" + //
                "    </Rule>\r\n" + //
                "</LifecycleConfiguration>\r\n"
                ;

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] localMd5Binary = md.digest(dd.getBytes());
        String base64Md5 = Base64.getMimeEncoder().encodeToString(localMd5Binary);

        log.info("asdfasfd {}", base64Md5);

    }

    @XmlRootElement(name = "CreateBucketConfiguration", namespace = "http://obs.region.example.com/doc/2015-06-30/")
    @Data
    public static class Dd {
        private String Name;

        private Integer Age;

        private List<String> list;

    }

    @Data
    @AllArgsConstructor
    public static class AO {

        
        private String a;

        @Sensitive
        private String c;
    }

}
