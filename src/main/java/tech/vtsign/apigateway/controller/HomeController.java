package tech.vtsign.apigateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @Value("${tech.vtsign.api-url}")
    private String apiUrl;
    @Value("${springdoc.swagger-ui.path}")
    private String apiDocsPath;

    @GetMapping("/")
    public Map<String, String> home() {
        HashMap<String, String> map = new HashMap<>();
        map.put("base_url", apiUrl);
        map.put("documentation", apiUrl + apiDocsPath);
        return map;
    }
}
