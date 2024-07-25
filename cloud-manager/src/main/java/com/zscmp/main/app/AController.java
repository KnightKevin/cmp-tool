package com.zscmp.main.app;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zscmp.main.app.model.Res;

@RestController
public class AController {

    @LogRequest
    @PostMapping("/a")
    public String a(@RequestBody Res res) {


        return "ok";
    }
}
