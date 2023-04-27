package com.multicampus.topicsation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorReviewDTO {
    private String name;
    private String reviewContent;
    private String reviewDate;
    private String tuteeId;
}
