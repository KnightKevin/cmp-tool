package com.zscmp.main.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zscmp.main.app.model.Res;

@RestController
public class AController {

    @LogRequest
    @GetMapping("/a")
    public String a() {


        return "ok";
    }
}
