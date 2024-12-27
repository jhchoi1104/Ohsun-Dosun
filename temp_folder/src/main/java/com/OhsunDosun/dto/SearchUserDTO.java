package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDTO {
    private int userId; // 사용자 ID
    private String name; // 사용자 이름
}
