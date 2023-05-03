package com.multicampus.topicsation.dto.pageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    private String name;
    private String interest;
    private String date;

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 6)
    @Max(value = 100)
    @Positive
    private int size = 6;

    public int getSkip(){
        return (page - 1) * 6;
    }
}
