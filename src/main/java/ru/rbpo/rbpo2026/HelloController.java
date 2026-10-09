package ru.rbpo.rbpo2026;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${greeting.text}")
    private String greetingText;

    @GetMapping("/hello")
    public String hello() {
        return greetingText;
    }

    @GetMapping("/number")
    public int number() {
        return 1337;
    }
}
