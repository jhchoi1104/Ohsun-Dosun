package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.Favorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoritesMapper {

    // 별칭 존재 여부 체크
    boolean existsByUserIdAndNickname(@Param("userId") Long userId, @Param("nickname") String nickname);

}
