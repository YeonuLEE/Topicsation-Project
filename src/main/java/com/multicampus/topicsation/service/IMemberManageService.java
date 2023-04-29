package com.multicampus.topicsation.service;

import com.multicampus.topicsation.dto.LoginDTO;
import java.util.Map;

public interface IMemberManageService {
   public LoginDTO login(Map<String, String> map) throws Exception;
}
