package com.example.JabaVeans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContactController {
    @RequestMapping("/contact")
    public ModelAndView contact(){
        ModelAndView mv = new ModelAndView("contact");
        return mv;
    }
}
