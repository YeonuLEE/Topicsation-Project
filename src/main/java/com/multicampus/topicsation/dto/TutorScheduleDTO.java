package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorScheduleDTO {
    private String class_id;
    private String class_date;
    private String class_time;
    private String tutee_id;
    private String tutee_name;
    private String tutor_id;
}
