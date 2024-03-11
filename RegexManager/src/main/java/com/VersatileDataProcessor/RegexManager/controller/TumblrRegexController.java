package com.VersatileDataProcessor.RegexManager.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RegexController {

    @GET
    @RequestMapping
    public Map<String, Object> homePageRequest() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "You Hit Regex-Manager:1.0.0");
        response.put("success", true);
        return response;
    }

    @POST
    @RequestMapping("/regex")
    public boolean addRegexExpression(RequestBody req) {
        System.out.println(req);
        return true;
    }
}
