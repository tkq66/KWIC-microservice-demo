package com.cvise.input.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cvise.input.service.InputService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
	InputService service;

    public MainController(InputService service) {
        this.service = service;
    }
    
    @GetMapping(path = "/Input/{id}",
				produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<String> getById(@PathVariable String id) {
    	return this.service.getInputById(id);
    }
    
    @GetMapping(path = "/Input/File/{id}",
    			produces = {MediaType.TEXT_PLAIN_VALUE})
    public String getLinkToFileOfCorpus(@PathVariable String id, HttpServletRequest request) {
    	String urlToFile = this.service.saveInputToFileById(id, request);
    	return urlToFile;
    }

    @PostMapping(path = "/Input/File",
				 consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
				 produces = {MediaType.TEXT_PLAIN_VALUE})
    public String storeDataFromFile(@RequestBody MultipartFile input, HttpServletRequest request) {
		String storedDataLink = this.service.saveInput(input, request);
        return storedDataLink;
    }
    
    @PostMapping(path = "/Input/Text",
				 consumes = {MediaType.TEXT_PLAIN_VALUE},
				 produces = {MediaType.TEXT_PLAIN_VALUE})
    public String storeDataFromText(@RequestBody String input, HttpServletRequest request) {
		String storedDataLink = this.service.saveInput(input, request);
        return storedDataLink;
    }
    
    @PostMapping(path = "/Input/List",
				 consumes = {MediaType.APPLICATION_JSON_VALUE},
				 produces = {MediaType.TEXT_PLAIN_VALUE})
    public String storeDataFromTextList(@RequestBody List<String> input, HttpServletRequest request) {
		String storedDataLink = this.service.saveInput(input, request);
        return storedDataLink;
    }
}
