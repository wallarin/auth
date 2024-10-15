package com.api.auth.service;

import com.api.auth.entity.Test;
import com.api.auth.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository repository) {
        this.testRepository = repository;
    }

    public List<Test> getAllTestData() {
        return testRepository.findAll();
    }
}
