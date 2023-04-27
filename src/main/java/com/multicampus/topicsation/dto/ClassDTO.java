package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDTO {
    private String class_id;
    private String class_date;
    private String class_time;
    private String tutee_id;
    private String name;
    private String tutor_id;
}