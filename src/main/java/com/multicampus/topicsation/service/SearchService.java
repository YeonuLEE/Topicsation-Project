package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import com.multicampus.topicsation.repository.ISearchDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements ISearchService{

    @Autowired
    private final ISearchDAO dao;


    @Override
    public List<SearchDTO> allList(PageReqeustDTO pageReqeustDTO) {

        return dao.allListDAO(pageReqeustDTO);
    }

    @Override
    public List<SearchDTO> searchList(PageReqeustDTO pageReqeustDTO) {

        return dao.searchListDAO(pageReqeustDTO);
    }

    @Override
    public int searchCount(PageReqeustDTO pageReqeustDTO) {
        return dao.searchCountDAO(pageReqeustDTO);
    }
}
