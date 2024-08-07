package com.zscmp.main.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WxRestController {



    @GetMapping("/wx/sendd")
    public String send() {

        String cropId = "d";
        String agentId = "d";
        String secret = "d-d";

        String userId = "w8000296";

        String url  = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token?access_token=FESJDBDmA8QfB0Pzss12_Atkb6Y1e7qgdT2LQDyxg7-6tjWsad7So-UmETFpmttLG-hfP5_s3c2SrLHZyB2FuBJYZWJuOe6QrsPddzVMYuPD9Sl8UaqM5Okmmhq2_sNEJ5nt2uGm-kf30zH9LKF9KRhQNZz78QLhVwcMUAip0wKits887aVsq07HNtR-lUQRxhLnvtPUZh_4Dh8U_HnHVw";


        return "ok";
    }
}
