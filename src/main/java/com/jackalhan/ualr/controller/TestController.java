package com.jackalhan.ualr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by txcakaloglu on 5/6/16.
 */
@Controller
public class TestController {
    @RequestMapping("/general")
    public String general() {

        return "general";
    }
    @RequestMapping("/")
    public String index(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    @RequestMapping("/index")
    public String index2(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    @RequestMapping("/basic_form")
    public String basicForm() {

        return "basic_form";
    }
    @RequestMapping("/simple")
    public String simple() {

        return "simple";
    }

 @RequestMapping("/login")
    public String login() {

        return "login";
    }
}
