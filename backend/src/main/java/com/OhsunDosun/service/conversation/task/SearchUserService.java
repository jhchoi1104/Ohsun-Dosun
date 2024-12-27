package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.SearchUserDTO;
import com.OhsunDosun.mapper.SearchUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchUserService {
    private final SearchUserMapper userMapper;

    // 사용자 이름으로 ID 조회
    public SearchUserDTO findUserIdByName(String name) {
        return userMapper.findUserByName(name);
    }
}
