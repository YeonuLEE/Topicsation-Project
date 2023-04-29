package com.multicampus.topicsation.dto.pageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReqeustDTO {

    private int currentPage; // 요청한 페이지

    private int dataPerPage = 6; // 페이지당 출력할 데이터 갯수
    private int pagePerOnce = 5; // 하나의 페이지리스트의 페이지 수

    private String name;
    private String interest;
    private String class_date;
}
