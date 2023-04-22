package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.TutorMyPageDTO;
import com.multicampus.topicsation.dto.TutorMypageScheduleDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.repository.IMemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService implements IMyPageService{

    @Autowired
    IMemberDAO dao;

    @Override
    public TutorMyPageDTO view(String user_id) {
        return dao.viewTutor(user_id);
    }

    @Override
    public int modify(TutorMyPageDTO tutorMyPageDTO) {
        return dao.modifyTutor(tutorMyPageDTO);
    }

    @Override
    public TutorMypageScheduleDTO tutorProfile(String user_id) {
        return dao.tutorProfile(user_id);
    }

    @Override
    public List<TutorScheduleDTO> schedule(String user_id) {
        return dao.scheduleDTO(user_id);
    }


}
