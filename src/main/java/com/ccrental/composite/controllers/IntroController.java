package com.ccrental.composite.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/intro", method = RequestMethod.GET)
public class IntroController {
    @RequestMapping(
            value = "/",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String introIndexGet(HttpServletRequest request, HttpServletResponse response) {
        return "intro/index";
    }
}