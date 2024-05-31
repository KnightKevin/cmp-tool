package com.zscmp.main.app.test;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ATest {

    @Test
    public void aaTest() {
        List<String> list = Arrays.asList(
            "csrf_token=IjNmNTQ1NDkzNGUyYTBiNWVlODNlODdiNDk0MTk3YmQ0MzAxY2UzMDUi.ZlhRog.ESops-pyiZ8T90cqFIsF6BV3wFM; Secure; Path=/", 
        "sessionId=IjNmNTQ1NDkzNGUyYTBiNWVlODNlODdiNDk0MTk3YmQ0MzAxY2UzMDUi.ZlhRog.ESops-pyiZ8T90cqFIsF6BV3wFM; Secure; Path=/"
        );


        log.info("dadfasdf {}", ddd(list));
    }


    public String ddd(List<String> cookies) {
            // 从响应中获取Set-Cookie头部
        // HttpHeaders responseHeaders = response.getHeaders();
        // List<String> cookies = responseHeaders.get(HttpHeaders.SET_COOKIE);

        if (cookies != null) {
            for (String cookie : cookies) {
                // 解析cookie字符串，假设cookie格式为 "name=value; Path=/; HttpOnly"
                String[] cookieParts = cookie.split(";");
                for (String part : cookieParts) {
                    if (part.trim().startsWith("csrf_token=")) {
                        String cookieValue = part.split("=")[1];
                        return cookieValue;
                    }
                }
            }
        }

        throw new RuntimeException("error!");
    }

}
