package com.multicampus.topicsation.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ISearchService {
    ResponseEntity<Map<String, Object>> searchList(Map<String, String> requestParams);
}
