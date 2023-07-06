package com.koheisaito.sk4j;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Component
public class ClientController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
