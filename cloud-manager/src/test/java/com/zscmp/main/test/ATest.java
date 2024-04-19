package com.zscmp.main.test;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
public class ATest {


    @Test
    public void test1() {
        A a = new A();
        a.setId("asdf");
        a.setName("name");

        JSONObject json = (JSONObject)JSONObject.toJSON(a);

        System.out.println("asfdasdf"+ json.toJSONString());
    }

    @Data
    private static class A {
        String name;
        String id;
    }

}
