package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;

import java.util.List;

public interface ISearchService {
    List<SearchDTO> allList(PageReqeustDTO pageReqeustDTO);
    List<SearchDTO> searchList(PageReqeustDTO pageReqeustDTO);
    int searchCount(PageReqeustDTO pageReqeustDTO);
}
