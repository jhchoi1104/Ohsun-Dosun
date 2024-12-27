package com.OhsunDosun.member.mapper;

import com.OhsunDosun.member.dto.Member;
import org.mapstruct.Mapper;

@Mapper
public interface MemberMapper {
    Member selectByName(String username);
    int insertMember(Member member);
}
