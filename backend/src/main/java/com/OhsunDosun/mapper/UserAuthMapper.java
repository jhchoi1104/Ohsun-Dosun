package com.OhsunDosun.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserAuthMapper {
    Integer getPasswordByUserId(@Param("accountId") int accountId);
}
