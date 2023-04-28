package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MypageScheduleDTO {
    private String user_id;
    private String name;
    private String profileimg;
    List<ClassDTO> scheduleDTOList;
}
