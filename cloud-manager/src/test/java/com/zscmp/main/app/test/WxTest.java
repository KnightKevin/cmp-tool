package com.zscmp.main.app.test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zscmp.main.app.App;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest(classes = App.class)
@Slf4j
public class WxTest {

    String cropId = "ww2ae67a72a1a1ea16";
    Integer agentId = 1000089;
    String secret = "evOtm_YQMMC5RcvygaFLoFNUeF_Cv5a5-3DVQti3XJA";
    private String accessToken = "FESJDBDmA8QfB0Pzss12_Atkb6Y1e7qgdT2LQDyxg7-6tjWsad7So-UmETFpmttLG-hfP5_s3c2SrLHZyB2FuBJYZWJuOe6QrsPddzVMYuPD9Sl8UaqM5Okmmhq2_sNEJ5nt2uGm-kf30zH9LKF9KRhQNZz78QLhVwcMUAip0wKits887aVsq07HNtR-lUQRxhLnvtPUZh_4Dh8U_HnHVw";

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void aTest() throws IOException {
        String content = "content";

        String userId = "w8000296";

        sendMessage(userId, content);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void sendMessage(String toUser, String content) {

        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s", getAccessToken());
        Map<String, Object> message = new HashMap<>();
        message.put("touser", toUser);
        message.put("msgtype", "text");
        message.put("agentid", agentId);
        Map<String, String> text = new HashMap<>();
        text.put("content", content);
        message.put("text", text);

        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, message, JSONObject.class);

        JSONObject body = getBody(response);

        log.info("ok");
    }


    private JSONObject getBody(ResponseEntity<JSONObject> response) {
        HttpStatus httpStatus = response.getStatusCode();
        if (HttpStatus.OK != httpStatus) {
            throw new RuntimeException("get token error");
        }
        JSONObject body  = response.getBody();
        int errcode = body.getInteger("errcode");

        if (errcode == 0) {
            return body;
        }

        if (errcode == 60020) {
            throw new RuntimeException(String.format("cmp所在的ip须要配置企业微信的“可信域名”，配置好后在企业微信控制台将ip/域名填到”企业可信IP“中 %s", body.toJSONString()));
        }

        throw new RuntimeException(String.format("企业微信接口请求错误 %s", body.toJSONString()));

    }



}

