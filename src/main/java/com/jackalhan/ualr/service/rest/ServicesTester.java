package com.jackalhan.ualr.service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by txcakaloglu on 5/4/16.
 */
@RestController
public class ServicesTester {

    @RequestMapping("/servicetester/genericLogin")
    public String index() {
        return "Welcome to the home page!";
    }
}
