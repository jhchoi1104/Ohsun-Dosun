package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.Favorites;
import com.OhsunDosun.mapper.FavoritesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesMapper favoritesMapper;

    // 별칭 테이블에 존재하는지 여부 체크
    public boolean isFavoriteExists(Long userId, String nickname) {
        return favoritesMapper.existsByUserIdAndNickname(userId, nickname);
    }
    //별칭 저장
    public void saveNickname(Long userID, Favorites nickname){
        favoritesMapper.insertNickname(userID, nickname.getFriend_id(), nickname.getNickname());
    };


}