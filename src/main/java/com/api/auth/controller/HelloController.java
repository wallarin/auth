package com.api.auth.controller;

import com.api.auth.entity.Test;
import com.api.auth.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    private final TestService testService;

    public HelloController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/api2/test")
    public String hello() {
        return "OK";
    }

    @GetMapping("/api/tests")
    public List<Test> getTests() {
        return testService.getAllTestData();
    }

}
