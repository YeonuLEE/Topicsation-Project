package com.multicampus.topicsation.controller;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/400")
    public void error400() {}

    @GetMapping("/401")
    public void error401() {}

    @GetMapping("/404")
    public void error404() {}

    @GetMapping("/500")
    public void error405() {}
}
