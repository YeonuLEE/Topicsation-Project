package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageReqeustDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISearchDAO {
    List<SearchDTO> allListDAO(PageReqeustDTO pageReqeustDTO);
    List<SearchDTO> searchListDAO(PageReqeustDTO pageReqeustDTO);
    int searchCountDAO(PageReqeustDTO pageReqeustDTO);
}