package com.multicampus.topicsation.repository;

import com.multicampus.topicsation.dto.SearchDTO;
import com.multicampus.topicsation.dto.pageDTO.PageRequestDTO;
import com.multicampus.topicsation.dto.pageDTO.PageResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISearchDAO {
    List<SearchDTO> searchListDAO(PageRequestDTO pageRequestDTO);
    int searchCountDAO(PageRequestDTO pageRequestDTO);
}
