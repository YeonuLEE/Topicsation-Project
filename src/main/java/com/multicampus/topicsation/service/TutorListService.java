package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MemberDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.repository.IMemberDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TutorListService implements ITutorListService {

    @Autowired
    IMemberDAO memberDAO;

    @Override
    public TutorViewDTO tutorInfo(Map<String, Object> paramMap, TutorViewDTO tutorViewDTO){
        tutorViewDTO = memberDAO.tutorInfo(paramMap);
        tutorViewDTO.setClassTimeList(memberDAO.tutorSchedule(paramMap));

        return tutorViewDTO;
    }

    @Override
    public boolean ClassReservate(Map<String, Object> paramMap) {
        int result = memberDAO.classReservate(paramMap);

        if(result == 1)
            return true;
        else
            return false;
    }
}
