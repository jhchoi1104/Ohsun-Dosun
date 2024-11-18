package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorites {
    private long favorite_id;
    private long user_id;
    private long friend_id;
    private String nickname;
    private String created_at;
}
