package com.ccrental.composite.cs.apis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/")
public class RootController {

    @ExceptionHandler
    public void handleException(HttpServletResponse response, Exception exception) throws IOException {
        try {
            response.getWriter().print(exception.toString());
        } catch (Exception ignored) {}
    }
}
