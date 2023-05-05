package com.multicampus.topicsation.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.util.Map;

public interface ILessonService {
    JSONObject getNewsService(String classId) throws ParseException;
    int evaluateService(Map<String,String> evaluateInfo);
    int reviewService(Map<String,String> reviewInfo);
    JSONObject getMembersService(String classId);
}
