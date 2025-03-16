package com.structura.steel.productservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/product-demo")
    public String demo() {
        return "Hello World from ProductService";
    }
}
