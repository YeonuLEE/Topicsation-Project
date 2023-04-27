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
    public int modify_tutor(MyPageDTO myPageDTO) {
        System.out.println(myPageDTO);
        dao.modifyTutor(myPageDTO);
        dao.modifyTutor2(myPageDTO);
        return 0;
    }

    @Override
    public MypageScheduleDTO tutorProfile(String user_id) {
        return dao.tutorProfile(user_id);
    }

    @Override
    public List<ClassDTO> schedule_tutor(String user_id) {
        return dao.schedule(user_id);
    }

    @Override
    public void delete_tutor(String user_id) {
        dao.deleteClass(user_id);
        dao.tutorDislike(user_id);
        dao.deleteTutorInfo(user_id);
        dao.deleteTutor(user_id);

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
        return dao.scheduleTutee(user_id);
    }

    @Override
    public List<ClassDTO> history_tutee(String user_id) {
        return dao.historyTutee(user_id);
    }

    @Override
    public void delete_tutee(String user_id) {
        dao.deleteTutee(user_id);
        dao.chageClass(user_id);
        dao.tuteeDislike(user_id);
    }

    @Override
    public void schedule_cancel(String class_id) {
        dao.cancelSchedule(class_id);
    }

    @Override
    public List<MyPageDTO> view_admin() {
        List<MyPageDTO> list =dao.viewAdmin();
//        System.out.println("List : "+list);
        return list;
    }

    @Override
    public void success(String user_id) {
        dao.successAdmin(user_id);
    }

    @Override
    public void fail(String user_id) {
        dao.failAdmin2(user_id);
        dao.failAdmin(user_id);
    }


}
