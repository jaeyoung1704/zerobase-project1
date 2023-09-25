package com.zerobase.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String index() throws Exception {
	log.info("홈");
	return "index";
    }
}
