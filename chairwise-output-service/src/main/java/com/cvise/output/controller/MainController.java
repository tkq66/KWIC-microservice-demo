package com.cvise.output.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cvise.output.service.OutputService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1")
public class MainController {
	OutputService service;

    public MainController(OutputService service) {
        this.service = service;
    }
	
	@GetMapping(path = "/Sorting/CircularShift/File/{id}",
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

	@GetMapping(path = "/Sorting/CircularShift/File/Stream/{id}",
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource outputFileStreamFromDataGenFromApi(
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
		return new FileSystemResource(localFilePath);
	}

	
	@GetMapping(path = "/Sorting/CircularShift/Text/{id}",
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
	
	@GetMapping(path = "/Sorting/CircularShift/List/{id}",
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
