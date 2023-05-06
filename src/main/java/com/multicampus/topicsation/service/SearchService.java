package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.repository.ISearchDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{

    @Autowired
    private final ISearchDAO dao;

    @Override
    public PageResponseDTO<SearchDTO> searchList(PageRequestDTO pageRequestDTO) {
        List<SearchDTO> searchDTOList = dao.searchListDAO(pageRequestDTO);
        int totalcount = dao.searchCountDAO(pageRequestDTO);

        return PageResponseDTO.<SearchDTO>withAll()
                .searchDTOList(searchDTOList)
                .total(totalcount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
