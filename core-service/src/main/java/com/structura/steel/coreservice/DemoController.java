package com.structura.steel.coreservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo() {
        return "Hello World";
    }

    @GetMapping("/demo/2")
    public String demo2() {
        return "Hello World - 2";
    }
}
