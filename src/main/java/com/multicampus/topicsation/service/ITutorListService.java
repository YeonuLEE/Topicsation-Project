package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.RecommendDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;

import java.util.List;
import java.util.Map;

public interface ITutorListService {
    TutorViewDTO tutorInfo(Map<String,Object> paramMap, TutorViewDTO tutorViewDTO);
    boolean ClassReserve(Map <String,Object> paramMap);

    List<RecommendDTO> recommend(String user_id);
    List<RecommendDTO> Non_members();
}
