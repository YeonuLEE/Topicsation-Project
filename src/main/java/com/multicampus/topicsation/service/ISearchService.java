package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;

import java.util.List;

public interface ISearchService {
    List<PageResponseDTO> allList(PageReqeustDTO pageReqeustDTO);
    List<PageResponseDTO> searchList(PageReqeustDTO pageReqeustDTO);
    int searchCount(PageReqeustDTO pageReqeustDTO);
}
