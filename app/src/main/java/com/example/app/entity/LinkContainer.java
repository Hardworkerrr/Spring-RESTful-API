package com.example.app.entity;

import java.util.LinkedHashMap;
import java.util.Map;
public class LinkContainer {
    private Map<String, String> links;

    public LinkContainer() {
        this.links = new LinkedHashMap<>();
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
