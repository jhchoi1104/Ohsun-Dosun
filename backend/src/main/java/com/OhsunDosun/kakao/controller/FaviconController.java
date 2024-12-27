package com.OhsunDosun.kakao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FaviconController {
    //favicon.ico 요청을 처리하는 핸들러
    @RequestMapping("favicon.ico")
    public void returnFavicon(){
        //빈 메서드로 favicon.ico요청을 처리
    }
}
