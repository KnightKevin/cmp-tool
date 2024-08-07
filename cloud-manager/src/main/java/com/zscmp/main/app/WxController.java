package com.zscmp.main.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WxController {

    @GetMapping("/wx/a")
    public String home(Model model) throws UnsupportedEncodingException {

        String cropId = "d";
        String agentId = "d";
        String secret = "d";

        String url  = "https://open.weixin.qq.com/connect/oauth2/authorize";

        //appid=ww5581718d4eff583d&agentid=1000017&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_base&state=STATE

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("appid", cropId)
            .queryParam("agentid", agentId)
            .queryParam("redirect_uri", "")
            .queryParam("response_type", "code")
            .queryParam("scope", "snsapi_base")
            .queryParam("state", "STATE")
            .encode()
            ;

        model.addAttribute("wxOauth2Url", builder.toUriString()+"#wechat_redirect");
        return "home";
    }

    @GetMapping("/wx/send")
    public String send() throws UnsupportedEncodingException {

        String cropId = "ww2ae67a72a1a1ea16";
        String agentId = "1000089";
        String secret = "evOtm_YQMMC5RcvygaFLoFNUeF_Cv5a5-3DVQti3XJA";

        String url  = "https://open.weixin.qq.com/connect/oauth2/authorize";

        //appid=ww5581718d4eff583d&agentid=1000017&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_base&state=STATE

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("appid", cropId)
            .queryParam("agentid", agentId)
            .queryParam("redirect_uri", "")
            .queryParam("response_type", "code")
            .queryParam("scope", "snsapi_base")
            .queryParam("state", "STATE")
            .encode()
            ;

        model.addAttribute("wxOauth2Url", builder.toUriString()+"#wechat_redirect");
        return "home";
    }
}
