package com.ccrental.composite.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(value = "/", method = RequestMethod.GET)
public class RootController {
    @RequestMapping(
            value = "/",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String rootIndexGet(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }
}