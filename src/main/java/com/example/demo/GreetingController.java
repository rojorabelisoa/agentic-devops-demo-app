package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    static final int MAX_NAME_LENGTH = 50;

    public record Greeting(String message) {
    }

    @GetMapping("/api/greeting")
    public Greeting greeting(@RequestParam(defaultValue = "world") String name) {
        String safeName = name.strip();
        if (safeName.isEmpty()) {
            safeName = "world";
        }
        if (safeName.length() > MAX_NAME_LENGTH) {
            safeName = safeName.substring(0, MAX_NAME_LENGTH);
        }
        return new Greeting("Hello, " + safeName + "!");
    }
}
