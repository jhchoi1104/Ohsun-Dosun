package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.SearchUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchUserMapper {
    SearchUserDTO findUserByName(@Param("name") String name);
}
