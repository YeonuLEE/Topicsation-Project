package com.multicampus.topicsation.dto.pageDTO;

import com.multicampus.topicsation.dto.SearchDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO <E> {
    private int page; // 현재 페이지
    private int size; // 한 페이지당 글의 개수
    private int total; // 전체 글의 개수

    private int start; // 그룹의 시작 페이지 넘버
    private int end; // 그룹의 시작 끝 넘버

    private boolean prev; //이전이 존재하는지 (1페이지는 이전이 없음)
    private boolean next; //다음이 존재하는지 (마지막페이지는 다음이 없음)

    private List<SearchDTO> searchDTOList;


    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<SearchDTO> searchDTOList, int total) {
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.searchDTOList = searchDTOList;

        this.end = (int)(Math.ceil(this.page / 6.0)) * 6;
        this.start = this.end - 5;

        int last = (int)(Math.ceil((total/(double)size)));

        this.end = end > last ? last: end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
