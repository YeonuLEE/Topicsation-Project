package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorMypageScheduleDTO {
    private String tutor_id;
    private String name;
    private String profileimg;
}
