package tech.vtsign.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @GetMapping("/")
    public Map<String, String> home() {
        HashMap<String, String> map = new HashMap<>();
        map.put("base_url", "https://api.vtsign.tech/");
        map.put("documentation", "https://api.vtsign.tech/api-docs");
        return map;
    }

}
