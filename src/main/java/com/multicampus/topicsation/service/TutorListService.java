package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.MemberDTO;
import com.multicampus.topicsation.dto.RecommendDTO;
import com.multicampus.topicsation.dto.TutorViewDTO;
import com.multicampus.topicsation.repository.ITutorListDAO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TutorListService implements ITutorListService {

    @Autowired
    private ITutorListDAO tutorListDAO;

    @Autowired
    private IS3FileService s3FileService;

    @Override
    public String tutorInfo(String tutorId, String calendarDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tutorId", tutorId);
        paramMap.put("classDate", calendarDate);

        TutorViewDTO tutorViewDTO = tutorListDAO.tutorInfo(paramMap);
        tutorViewDTO.setClassTimeList(tutorListDAO.tutorSchedule(paramMap));
        tutorViewDTO.setTutorReviewList(tutorListDAO.tutorReview(paramMap.get("tutorId").toString()));

        JSONObject jsonObject_info = new JSONObject();
        JSONArray jsonArray_schedule = new JSONArray();
        JSONArray jsonArray_Review = new JSONArray();

        String bucketName = "asset";
        String folderName = "profile";

        // 확장자를 제외한 파일 이름
        String imgId = tutorListDAO.getTutorImg(paramMap);
        String profileImgUrl = s3FileService.getImageUrl(bucketName, folderName, imgId);

        jsonObject_info.put("user_id",tutorId);
        jsonObject_info.put("name", tutorViewDTO.getName());
        jsonObject_info.put("nationality", tutorViewDTO.getNationality());
        jsonObject_info.put("introduce", tutorViewDTO.getInfo());
        jsonObject_info.put("like", tutorViewDTO.getLike());
        jsonObject_info.put("picture", profileImgUrl);
        jsonObject_info.put("interest1", tutorViewDTO.getInterest1());
        jsonObject_info.put("interest2", tutorViewDTO.getInterest2());

        for(int i = 0; i<tutorViewDTO.getClassTimeList().size(); i++) {
            JSONObject jsonObject_schedule = new JSONObject();
            jsonObject_schedule.put("class_id", tutorViewDTO.getClassTimeList().get(i).getClass_id());
            jsonObject_schedule.put("class_date", tutorViewDTO.getClassTimeList().get(i).getClass_date());
            jsonObject_schedule.put("class_time", tutorViewDTO.getClassTimeList().get(i).getClass_time());
            jsonObject_schedule.put("tutee_id", tutorViewDTO.getClassTimeList().get(i).getTutee_id());
            jsonObject_schedule.put("tutor_id", tutorViewDTO.getClassTimeList().get(i).getTutor_id());
            jsonArray_schedule.add(jsonObject_schedule);
        }

        for(int i = 0; i<tutorViewDTO.getTutorReviewList().size(); i++){
            JSONObject jsonObject_Review = new JSONObject();
            jsonObject_Review.put("tutee_name", tutorViewDTO.getTutorReviewList().get(i).getName());
            jsonObject_Review.put("review_date", tutorViewDTO.getTutorReviewList().get(i).getReviewDate());
            jsonObject_Review.put("review_content", tutorViewDTO.getTutorReviewList().get(i).getReviewContent());
            jsonArray_Review.add(jsonObject_Review);
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("tutor_info", jsonObject_info);
        jsonObject.put("schedule", jsonArray_schedule);
        jsonObject.put("review",jsonArray_Review);

        return jsonObject.toJSONString();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public int ClassReserve(Map<String, String> paramMap) {
        String tutorId = paramMap.get("tutorId");
        String classDate = paramMap.get("classDate");
        String classTime = paramMap.get("classTime");

        String classId = tutorId + "_" + classDate + "_" + classTime;

        paramMap.put("classId", classId);
        return tutorListDAO.classReserve(paramMap);
    }

    @Override
    public String tutor_recommend(String userId) {
        List<RecommendDTO> list;
        if(!userId.equals("default")){
            list = recommend(userId);
        }else {
            list = tutorListDAO.nonMembers();
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        String bucketName = "asset";
        String folderName = "profile";

        for(RecommendDTO dto : list){
            JSONObject object = new JSONObject();
            object.put("user_id",dto.getUser_id());
            object.put("name",dto.getName());
            object.put("tutor_image",s3FileService.getImageUrl(bucketName, folderName, dto.getProfileImg()));
            object.put("like",dto.getLike());
            object.put("nationality",dto.getNationality());
            object.put("interest1",dto.getInterest1());
            object.put("interest2",dto.getInterest2());

            jsonArray.add(object);
        }

        jsonObject.put("tutor_list",jsonArray);

        return jsonObject.toJSONString();
    }

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
}
