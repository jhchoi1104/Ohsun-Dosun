package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.Favorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface FavoritesMapper {

    // 별칭 존재 여부 체크
    boolean existsByUserIdAndNickname(@Param("userId") long userId, @Param("nickname") String nickname);

    // 별칭 등록하기
    void insertNickname(@Param("userId") long userId,@Param("friendId") long friendId, @Param("nickname") String nickname);

}
