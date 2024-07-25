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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
public class WxTest {

    private String corpId = "x";
    private String agentId = "x";
    private String secret = "x";
    private String accessToken = "Mj3iiBGQ9aDtr4nU-BZsnu3y2bel4nNveIMagZsikXeF_dnrStCb9BRdI3KkmKxd-0pIdMN9IKHzGClQTcimpqZ2y1fmG7cnYpaIS7xJy1YlvXbp7B5WfgZ8n0rGhAHgQypMgoC8hO-r_TJefpfsCRtoVFXsNfPrWGw14R7g8fbrq4AH8ACsjU7xQ-R_OjQPsO6jkzJlcVt6DZi1ZWbyMg";

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void aTest() throws IOException {
        String toUser = "toUser";
        String content = "content";

        refreshToken();

        sendMessage(toUser, content);
    }

    private void refreshToken() {
        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s", corpId, secret);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity(url, JSONObject.class);
        HttpStatus httpStatus = response.getStatusCode();
        if (HttpStatus.OK != httpStatus) {
            throw new RuntimeException("get token error");
        }
        JSONObject body  = response.getBody();
        if (body.getInteger("errcode") != 0) {
            throw new RuntimeException(String.format("get token errCode %s", body.toJSONString()));
        }
        
        this.accessToken = (String) response.getBody().get("access_token");
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

