package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorScheduleDTO {
    private String class_id;
    private String class_date;
    private String class_time;
    private int tutee_id;
    private int tutor_id;
}
