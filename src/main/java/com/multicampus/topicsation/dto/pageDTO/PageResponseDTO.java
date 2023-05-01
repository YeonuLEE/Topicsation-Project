package com.multicampus.topicsation.dto.pageDTO;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO {
//    private List dataList; // 페이지에 보여질 데이터 리스트
    private int currentPage; // 현재 페이지

    private int dataPerPage; // 페이지당 출력할 데이터 갯수
    private int pagePerOnce; // 하나의 페이지리스트의 페이지 수

    private int totalDataCount; // 전체 데이터 갯수
    private int totalPageCount; // 전체 페이지 갯수

    private int startData; // 현재 페이지의 첫번째 데이터
    private int startPage; // 페이지리스트의 첫 페이지 번호
    private int endPage;  // 페이지리스트의 마지막 페이지 번호
    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부


    public PageResponseDTO(PageReqeustDTO pageReqeustDTO, int totalDataCount){
        this.currentPage = pageReqeustDTO.getCurrentPage();
        this.dataPerPage = pageReqeustDTO.getDataPerPage();
        this.pagePerOnce = pageReqeustDTO.getPagePerOnce();

        this.totalPageCount = (int)Math.ceil(totalDataCount / dataPerPage);
        this.startPage = (int)Math.floor(currentPage / pagePerOnce) + 1;
        this.endPage = startPage + pagePerOnce - 1;
        this.endPage = endPage > totalPageCount ? totalPageCount: endPage;
        this.prev = startPage > pagePerOnce + 1;
        this.next = totalPageCount > endPage;
    }

}
