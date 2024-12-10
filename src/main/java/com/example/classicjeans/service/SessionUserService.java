package com.example.classicjeans.service;

import com.example.classicjeans.dto.request.AlanDementiaRequest;
import com.example.classicjeans.dto.request.AlanQuestionnaireRequest;
import com.example.classicjeans.entity.FamilyInfo;
import com.example.classicjeans.entity.Users;
import org.springframework.stereotype.Service;

@Service
public class SessionUserService {

    // 세션에 저장된 유저로 request에 set
    public void setUserFromSession(Object selectedUserFromSession, String selectedTypeFromSession, Object request) {
        if (selectedUserFromSession != null && selectedTypeFromSession != null) {
            if ("user".equals(selectedTypeFromSession)) {
                Users selectedUser = (Users) selectedUserFromSession;
                if (request instanceof AlanQuestionnaireRequest) {
                    ((AlanQuestionnaireRequest) request).setUser(selectedUser);
                } else if (request instanceof AlanDementiaRequest) {
                    ((AlanDementiaRequest) request).setUser(selectedUser);
                }
            } else if ("family".equals(selectedTypeFromSession)) {
                FamilyInfo selectedFamilyInfo = (FamilyInfo) selectedUserFromSession;
                if (request instanceof AlanQuestionnaireRequest) {
                    ((AlanQuestionnaireRequest) request).setFamily(selectedFamilyInfo);
                } else if (request instanceof AlanDementiaRequest) {
                    ((AlanDementiaRequest) request).setFamily(selectedFamilyInfo);
                }
            }
        }
    }
}
