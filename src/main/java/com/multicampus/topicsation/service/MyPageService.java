package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MyPageDTO;
import com.multicampus.topicsation.dto.MypageScheduleDTO;
import com.multicampus.topicsation.dto.ClassDTO;
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
    public String check_role(String user_id) {
        return dao.checkRole(user_id);
    }

    @Override
    public MyPageDTO view_tutor(String user_id) {
        return dao.viewTutor(user_id);
    }

    @Override
    public int modify_tutor(MyPageDTO MyPageDTO) {
        return dao.modifyTutor(MyPageDTO);
    }

    @Override
    public MypageScheduleDTO tutorProfile(String user_id) {
        return dao.tutorProfile(user_id);
    }

    @Override
    public List<ClassDTO> schedule_tutor(String user_id) {
        return dao.scheduleDTO(user_id);
    }

    @Override
    public MyPageDTO view_tutee(String user_id) { return dao.viewTutee(user_id); }

    @Override
    public int modify_tutee(MyPageDTO myPageDTO) {
        return dao.modifyTutee(myPageDTO);
    }

    @Override
    public MypageScheduleDTO tuteeProfile(String user_id) {
        return dao.tuteeProfile(user_id);
    }

    @Override
    public List<ClassDTO> schedule_tutee(String user_id) {
        return dao.scheduleTuteeDTO(user_id);
    }

    @Override
    public List<ClassDTO> history_tutee(String user_id) {
        return dao.historyTuteeDTO(user_id);
    }


}
