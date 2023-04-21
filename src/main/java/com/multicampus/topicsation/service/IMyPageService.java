package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.TutorMyPageDTO;

public interface IMyPageService {
    TutorMyPageDTO view(String user_id);
    int modify(TutorMyPageDTO tutorMyPageDTO);
}
