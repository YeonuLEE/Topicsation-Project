package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPageScheduleDTO {
    private String user_id;
    private String name;
    private String profileImg;
    List<ClassDTO> scheduleDTOList;
}
