package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 뉴스 테이블과 대응되는 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private String category;
    private String newsJson;
}
