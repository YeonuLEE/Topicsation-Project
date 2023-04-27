package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MemberDTO;
import com.multicampus.topicsation.dto.TutorScheduleDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.repository.IMemberDAO;
import com.multicampus.topicsation.repository.ITutorListDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TutorListService implements ITutorListService {

    @Autowired
    ITutorListDAO tutorListDAO;

    @Override
    public TutorViewDTO tutorInfo(Map<String, Object> paramMap, TutorViewDTO tutorViewDTO){
        tutorViewDTO = tutorListDAO.tutorInfo(paramMap);
        tutorViewDTO.setClassTimeList(tutorListDAO.tutorSchedule(paramMap));
        tutorViewDTO.setTutorReviewList(tutorListDAO.tutorReview(paramMap.get("tutorId").toString()));

        return tutorViewDTO;
    }

    @Override
    public boolean ClassReserve(Map<String, Object> paramMap) {
        int result = tutorListDAO.classReserve(paramMap);

        if(result == 1)
            return true;
        else
            return false;
    }
}
