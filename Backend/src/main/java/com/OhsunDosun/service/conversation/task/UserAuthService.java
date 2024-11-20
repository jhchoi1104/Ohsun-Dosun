package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.mapper.UserAuthMapper;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    private final UserAuthMapper userAuthMapper;

    public UserAuthService(UserAuthMapper userAuthMapper) {
        this.userAuthMapper = userAuthMapper;
    }
    //비밀번호 검증하기
    public boolean verifyPassword(int accountId, int accountPassword) {
        //DB에서 저장된 비밀번호 가져오기
        Integer storedPW = userAuthMapper.getPasswordByUserId(accountId);
        if(storedPW == null) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다. 다시 입력해주세요");
        }
        //입력 비밀번호와 db 저장 비밀번호 비교
        return accountPassword == storedPW;
    }
}
