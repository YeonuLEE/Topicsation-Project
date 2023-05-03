package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;

import java.util.List;

public interface ISearchService {
    PageResponseDTO<SearchDTO> searchList(PageRequestDTO pageRequestDTO);
}
