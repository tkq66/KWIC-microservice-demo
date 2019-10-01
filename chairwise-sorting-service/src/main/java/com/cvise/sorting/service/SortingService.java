package com.cvise.sorting.service;

import com.cvise.sorting.pojo.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SortingService {
    public ApiResponse doStuff() {
        //do some processing here	
        return new ApiResponse(HttpStatus.OK.value(), "this works");
    }
}
