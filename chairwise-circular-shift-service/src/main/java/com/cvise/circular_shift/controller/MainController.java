package com.cvise.circular_shift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.cvise.circular_shift.service.CircularShiftService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
	@Value("${user.home}")
    public String uploadDir;
	CircularShiftService service;

    public MainController(CircularShiftService service) {
        this.service = service;
    }
    
    @GetMapping(path = "/CircularShift/{id}",
				produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> getKeywordsById(@PathVariable String id) {
		return this.service.getKeywordsById(id);
	}
    
    @PostMapping("/CircularShift/Input/File/{id}")
	public String performCircularShiftOnDataGenFromApi(
			@RequestHeader(value="data-file-creator-api") String urlToGetDataFile,
			@RequestHeader(value="data-file-creator-api-method") String methodToGetDataFile,
			@RequestHeader(value="data-file-creator-api-content-type") String contentTypeToGetDataFile,
			@PathVariable String id) {        
		String contextualSlash = (urlToGetDataFile.charAt(urlToGetDataFile.length() - 1) == '/')
									? ""
									: "/";
		String apiUrlToGetDataFile = urlToGetDataFile + contextualSlash + id;
		String localFilePath = service.uploadFileFromApiRequest(apiUrlToGetDataFile,
										 						methodToGetDataFile,
										 						contentTypeToGetDataFile);
		String dataId = service.storeAllKeywordsInContextFromFileToDatabase(localFilePath);
		return dataId;
    }
}
