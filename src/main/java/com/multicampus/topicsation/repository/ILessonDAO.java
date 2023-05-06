package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.NewsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ILessonDAO {

    // 튜터와 튜티의 관심사를 받아오는 메서드
    List<String> getInterestsDAO(String classId);
    // 관심사를 바탕으로 뉴스를 받아오는 메서드
    List<NewsDTO> getNewsDAO(Set<String> interestsSet);
    // 좋아요 시 tutor_info 테이블의 like를 1 증가시키는 메서드
    int likeDAO(String classId);
    // 싫어요 시 dislike 테이블에 튜티와 튜터 id를 추가하는 메서드
    int dislikeDAO(String classId);
    //리뷰 등록 메서드
    int reviewDAO(@Param("review_content") String review_content, @Param("classId") String classId);
    // 수업에 입장하는 멤버들을 걸러내기 위한 메서드
    Integer getMembersDAO1(String classId);
    Integer getMembersDAO2(String classId);
    // 수업 때 추천된 뉴스 URL을 테이블에 추가해주는 메서드
    void setURL(Map<String, Object> params);
}
