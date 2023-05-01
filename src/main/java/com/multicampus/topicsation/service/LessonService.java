package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.NewsDTO;
import com.multicampus.topicsation.repository.ILessonDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonService implements ILessonService{

    @Autowired
    ILessonDAO lessonDAO;

    @Override
    public JSONObject getNewsService(String classId) throws ParseException {
        JSONParser jsonParser = new JSONParser();

        // 수업에 참여중인 멤버들의 관심사 불러오기
        List<String> interests = lessonDAO.getInterestsDAO(classId);

        // 관심사 중복 카운트
        Map<String, Integer> interestCount = new HashMap<>();
        for(String interest : interests){
            if(!interestCount.containsKey(interest)){
                interestCount.put(interest, 1);
            }else{
                Integer count = interestCount.get(interest);
                interestCount.put(interest, ++count);
            }
        }

        // 카운트롤 기준으로 정렬
        Map<String, Integer> sortedMap = interestCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // 카운트 후 상위 두개만 골라서 뉴스 고르기
        Map<String, Integer> topTwo = sortedMap.entrySet().stream()
                .limit(2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // 카테고리만 뽑아내기
        Set<String> interestsSet = new HashSet<>(topTwo.keySet());
        interestsSet.add("today");

        // 카테고리 해당하는 뉴스 두개 가져오기
        List<NewsDTO> newsDTOList = lessonDAO.getNewsDAO(interestsSet);

        JSONObject jsonObject = new JSONObject();
        for(NewsDTO newsDTO : newsDTOList){
            String category = newsDTO.getCategory();
            String newsJsonString = newsDTO.getNewsJson();
            JSONObject newsJson = (JSONObject) jsonParser.parse(newsJsonString); // 파싱

            jsonObject.put(category, newsJson);
        }
        System.out.println("뉴스!: "+ jsonObject);

        // 뉴스 세개 JSONArray에 담아서 return
        return jsonObject;
    }

    @Override
    public int evaluateService(String likeOrDislike, String classId) {
        if(likeOrDislike.equals("like")){
            return lessonDAO.likeDAO(classId);
        }else if(likeOrDislike.equals("dislike")){
            return lessonDAO.dislikeDAO(classId);
        }
        System.out.println("service 실패");
        return 0;
    }

    @Override
    public int reviewService(String review_content, String classId) {
        return lessonDAO.reviewDAO(review_content, classId);
    }
}
