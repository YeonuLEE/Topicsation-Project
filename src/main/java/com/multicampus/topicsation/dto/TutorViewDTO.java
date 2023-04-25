package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorViewDTO {
    private String name;
    private String nationality;
    private String info;
    private int like;
    private String profileimg;
    private String interest1;
    private String interest2;
}
