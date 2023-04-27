package com.multicampus.topicsation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDTO {
    private String toAddress;
    private String title;
    private String message;
}
