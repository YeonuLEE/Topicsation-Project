package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.TutorMyPageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberDAO {

    TutorMyPageDTO viewTutor(String user_id);
    int modifyTutor(TutorMyPageDTO tutorMyPageDTO);

}
