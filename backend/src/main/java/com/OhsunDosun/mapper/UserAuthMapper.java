package com.OhsunDosun.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserAuthMapper {
    // 비밀번호 조회
    Integer getPasswordByUserId(@Param("accountId") int accountId);
}
