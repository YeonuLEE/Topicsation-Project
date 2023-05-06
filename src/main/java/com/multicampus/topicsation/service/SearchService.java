package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.repository.ISearchDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{

    @Autowired
    private final ISearchDAO dao;

    @Autowired
    private IS3FileService s3FileService;

    @Override
    public ResponseEntity<Map<String, Object>> searchList(Map<String, String> requestParams) {

        int page = Integer.parseInt(requestParams.getOrDefault("page", "1"));
        int size = Integer.parseInt(requestParams.getOrDefault("size", "6"));
        String name = requestParams.get("name");
        String interest = requestParams.get("interest");
        String date = requestParams.get("date");

        if(date != null){
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date dateFormat = inputFormat.parse(date);
                date = outputFormat.format(dateFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .name(name)
                .interest(interest)
                .date(date)
                .page(page)
                .size(size)
                .build();

        List<SearchDTO> searchDTOList = dao.searchListDAO(pageRequestDTO);
        for (SearchDTO searchDTO : searchDTOList) {
            String imgId = searchDTO.getProfileImg();
            String imageUrl = s3FileService.getImageUrl("asset", "profile", imgId);
            searchDTO.setProfileImg(imageUrl);
        }

        int totalcount = dao.searchCountDAO(pageRequestDTO);
        PageResponseDTO<SearchDTO> pageResponseDTO = PageResponseDTO.<SearchDTO>withAll()
                .searchDTOList(searchDTOList)
                .total(totalcount)
                .pageRequestDTO(pageRequestDTO)
                .build();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("all_list",pageResponseDTO.getSearchDTOList());
        resultMap.put("page",pageResponseDTO.getPage());
        resultMap.put("size",pageResponseDTO.getSize());
        resultMap.put("total",pageResponseDTO.getTotal());
        resultMap.put("start",pageResponseDTO.getStart());
        resultMap.put("end",pageResponseDTO.getEnd());
        resultMap.put("prev",pageResponseDTO.isPrev());
        resultMap.put("next",pageResponseDTO.isNext());

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }
}
