package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendDTO {
    private String user_id;
    private String name;
    private String profileImg;
    private String like;
    private String nationality;
    private String interest1;
    private String interest2;
}
