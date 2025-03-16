package com.structura.steel.partnerservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/partner-demo")
    public String demo() {
        return "Hello World from PartnerService";
    }
}
