package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.RecommendDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ITutorListService {
    String tutorInfo(String tutorId,String calendarDate);
    ResponseEntity<Void> ClassReserve(Map<String, String> paramMap);
    String tutor_recommend(String userId);
}
