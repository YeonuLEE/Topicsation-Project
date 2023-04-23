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


}
