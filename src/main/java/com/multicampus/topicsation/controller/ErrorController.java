package com.multicampus.topicsation.controller;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/404")
    public String lesson() {
        return "html/404";
    }
}
