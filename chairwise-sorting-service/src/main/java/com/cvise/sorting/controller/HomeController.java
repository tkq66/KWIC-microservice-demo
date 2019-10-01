package com.cvise.sorting.controller;

import com.cvise.sorting.pojo.ApiResponse;
import com.cvise.sorting.service.SortingService;
import com.cvise.sorting.service.SortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

    SortingService service;

    @Autowired
    public HomeController(SortingService service) {
        this.service = service;
    }

    @GetMapping("")
    public String helloWorld() {
        return "v1 version of sorting api";
    }

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    public ApiResponse doStuff(@RequestBody Map<String, Object> payload) {
        System.out.println("json body" + payload);
        ApiResponse apiResponse = service.doStuff();
        apiResponse.setStatusCode(apiResponse.getStatusCode());
        return apiResponse;
    }
}
