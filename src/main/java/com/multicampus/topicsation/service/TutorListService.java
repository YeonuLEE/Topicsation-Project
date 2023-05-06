package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MemberDTO;
import com.multicampus.topicsation.dto.RecommendDTO;
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
        String tutorId = paramMap.get("tutorId").toString();
        String classDate = paramMap.get("classDate").toString();
        String classTime = paramMap.get("classTime").toString();

        String classId = tutorId + "_" + classDate + "_" + classTime;

        paramMap.put("classId", classId);

        int result = tutorListDAO.classReserve(paramMap);

        if(result == 1)
            return true;
        else
            return false;
    }

    @Override
    public List<RecommendDTO> recommend(String user_id) {

        MemberDTO memberDTO = tutorListDAO.tuteeInterest(user_id);
        List<RecommendDTO> interestList = tutorListDAO.recommendList(user_id, memberDTO.getInterest1(), memberDTO.getInterest2());

        int size = 6 - interestList.size();
        if(size > 0) {
            List<RecommendDTO> spareList1 = tutorListDAO.spareList(user_id, memberDTO.getInterest1());
            List<RecommendDTO> spareList2 = tutorListDAO.spareList(user_id, memberDTO.getInterest2());

            for(RecommendDTO dto : spareList1) {
                if(interestList.size() >= 6) break;
                if(!interestList.contains(dto)) interestList.add(dto);
            }

            for(RecommendDTO dto : spareList2) {
                if(interestList.size() >= 6) break;
                if(!interestList.contains(dto)) interestList.add(dto);
            }
        }
        return interestList;
    }

    @Override
    public List<RecommendDTO> Non_members() {
        return tutorListDAO.nonMembers();
    }
}
