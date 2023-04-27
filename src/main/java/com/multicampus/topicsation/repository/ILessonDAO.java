package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.NewsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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
}
