package com.multicampus.topicsation.dto.pageDTO;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO {
    private List<Data> dataList; // 페이지에 보여질 데이터 리스트
    private int currentPage; // 현재 페이지

    private int dataPerPage; // 페이지당 출력할 데이터 갯수
    private int pagePerOnce; // 하나의 페이지리스트의 페이지 수

    private int totalDataCount; // 전체 데이터 갯수
    private int totalPageCount; // 전체 페이지 갯수

    private int startPage; // 페이지리스트의 첫 페이지 번호
    private int endPage;  // 페이지리스트의 마지막 페이지 번호
    private boolean prev; // 이전 페이지 존재 여부
    private boolean next; // 다음 페이지 존재 여부

    private void pageCalculation(PageReqeustDTO dto){
        this.currentPage = dto.getCurrentPage();

        totalPageCount = (int)Math.ceil((totalDataCount - 1) / dataPerPage);
        startPage = (int)Math.floor(currentPage / pagePerOnce) + 1;
        endPage = startPage + pagePerOnce - 1;
        endPage = endPage > totalPageCount ? totalPageCount: endPage;
        prev = startPage > 1;
        next = totalPageCount > endPage;
    }

}
