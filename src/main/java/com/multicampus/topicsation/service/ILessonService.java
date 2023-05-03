package com.multicampus.topicsation.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.List;

public interface ILessonService {
    JSONObject getNewsService(String classId) throws ParseException;
    int evaluateService(String likeOrDislike, String classId);
    JSONObject getMembersService(String classId);
}
