package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.Favorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface FavoritesMapper {
    boolean existsByUserIdAndNickname(@Param("userId") long userId, @Param("nickname") String nickname);
    void insertNickname(@Param("userId") long userId,@Param("friendId") long friendId, @Param("nickname") String nickname);
}
