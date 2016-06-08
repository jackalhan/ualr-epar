package com.jackalhan.ualr.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by txcakaloglu on 6/8/16.
 */
public class CustomErrorController implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(value=PATH)
    public String error() {
        return "Error heaven";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}