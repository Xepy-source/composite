package com.ccrental.composite.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/main", method = RequestMethod.GET)
public class MainController {
    @RequestMapping(
            value = "/",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String mainIndexGet(HttpServletRequest request, HttpServletResponse response) {
        return "main/index";
    }
}