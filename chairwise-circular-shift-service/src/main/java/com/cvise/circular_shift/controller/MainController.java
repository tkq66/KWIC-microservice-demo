package com.cvise.circular_shift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    
    @PatchMapping("/CircularShift/Input/File/{id}")
	public void downloadInputDataToLocalFile(
			@RequestHeader(value="data-file-creator-api") String urlToGetDataFile,
			@RequestHeader(value="data-file-creator-api-method") String methodToGetDataFile,
			@RequestHeader(value="data-file-creator-api-content-type") String contentTypeToGetDataFile,
			@PathVariable String id) {        
    	String localFileName = UUID.randomUUID().toString() + ".txt";
		String localFilePath = uploadDir + File.separator + localFileName;
		try {
			URL urlObj= new URL(urlToGetDataFile + "/" + id);
	    	HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
	    	con.setRequestMethod(methodToGetDataFile);
	    	con.setRequestProperty("Content-Type", contentTypeToGetDataFile);
//	    	int status = con.getResponseCode();
	    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			in.close();
			con.disconnect();
			String urlToFile = content.toString();

			ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(urlToFile).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(localFilePath);
	    	fileOutputStream.getChannel()
	    	  .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
	    	fileOutputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
}
