package com.OhsunDosun.member.mapper;

import com.OhsunDosun.member.dto.Member;

public interface MemberMapper {
    Member selectByName(String username);
}
