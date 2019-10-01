package com.cvise.circular_shift.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.cvise.circular_shift.exception.HttpRequestFailed;

@Service
public class CircularShiftService {
    public CircularShiftService() {
    }

    public String getStringFromHttpRequest(String url,
    									   String requestMethod,
    									   String contentType) {
    	try {
    		// Connect to the endpoint
    		URL urlObj= new URL(url);
        	HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        	con.setRequestMethod(requestMethod);
        	con.setRequestProperty("Content-Type", contentType);
        	
        	// Read result from the endpoint
        	BufferedReader in = new BufferedReader(
        							new InputStreamReader(
        									con.getInputStream()));
    		String inputLine;
    		StringBuffer content = new StringBuffer();
    		while ((inputLine = in.readLine()) != null) {
    		    content.append(inputLine);
    		}
    		
    		// Clean up
    		in.close();
    		con.disconnect();
    		
    		// Get the result
    		String urlToFile = content.toString();
    		return urlToFile;    		
    	} catch (Exception e) {
            e.printStackTrace();
            throw new HttpRequestFailed("Failed to make http request to "
            	+ url
            	+ " with request mehod "
            	+ requestMethod
            	+ " and content type "
            	+ contentType
                + ". Please try again!");
        }
    }
}
