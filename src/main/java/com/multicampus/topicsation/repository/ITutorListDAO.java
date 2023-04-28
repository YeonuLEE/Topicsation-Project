package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ITutorListDAO {

    // 튜터 상세 페이지 메서드
    TutorViewDTO tutorInfo(Map<String,Object> paramMap);
    List<TutorScheduleDTO> tutorSchedule(Map <String,Object> paramMap);
    List<TutorReviewDTO> tutorReview(String tutorId);
    int classReserve(Map <String,Object> paramMap);

    // 튜터 추천 메서드
    MemberDTO tuteeInterest(String user_id);
    List<RecommendDTO> recommendList(String interest1, String interest2);
    List<RecommendDTO> spareList(String interest);
    List<RecommendDTO> nonMembers();
}
