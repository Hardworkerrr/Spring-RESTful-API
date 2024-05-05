package com.example.app.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class LinkUtils {

    private LinkUtils() {

    }
    public static String createLink(String urlName, Map<String, Object> params) {
        Map<String, Object> parameters = new HashMap<>(params);
        switch (urlName){
            case "next":
                parameters.replace("pageNumber",(Integer) parameters.get("pageNumber")+1);
                break;
            case "prev":
                parameters.replace("pageNumber",(Integer) parameters.get("pageNumber")-1);
                break;
        }
        UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest().replaceQuery(null);
        parameters.forEach(builder::queryParam);
        return builder.build().toUriString();
    }
}
