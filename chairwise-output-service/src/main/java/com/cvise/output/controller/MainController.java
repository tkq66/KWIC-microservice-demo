package com.cvise.output.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvise.output.exception.FileReadException;
import com.cvise.output.exception.HttpFileTransferFailed;
import com.cvise.output.service.OutputService;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
	OutputService service;

    public MainController(OutputService service) {
        this.service = service;
    }
	
	@GetMapping(path = "/Output/Sorting/File/{id}",
				produces = {MediaType.TEXT_PLAIN_VALUE})
	public String outputFileNameFromDataGenFromApi(
			@RequestHeader(value="data-file-creator-api") String urlToGetDataFile,
			@RequestHeader(value="data-file-creator-api-method") String methodToGetDataFile,
			@RequestHeader(value="data-file-creator-api-content-type") String contentTypeToGetDataFile,
			@PathVariable String id, HttpServletRequest request) {        
		String contextualSlash = (urlToGetDataFile.charAt(urlToGetDataFile.length() - 1) == '/')
									? ""
									: "/";
		String apiUrlToGetDataFile = urlToGetDataFile + contextualSlash + id;
		String localFilePath = service.uploadFileFromApiRequest(apiUrlToGetDataFile,
										 						methodToGetDataFile,
										 						contentTypeToGetDataFile);
		String fileUrl = service.getStaticFileLocationFromFile(localFilePath, request);
		return fileUrl;
	}
	
	@GetMapping(path = "/Output/Sorting/Text/{id}",
				produces = {MediaType.TEXT_PLAIN_VALUE})
	public String outputStringFromDataGenFromApi(
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
		String output = service.getTextFromFile(localFilePath);
		return output;
	}
	
	@GetMapping(path = "/Output/Sorting/List/{id}",
				produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<String> outputListFromDataGenFromApi(
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
		List<String> output = service.getListFromFile(localFilePath);
		return output;
	}
}
