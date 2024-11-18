package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class historyDTO {
    private Integer TransactionId;
    private Integer SenderId;
    private Integer receiverId;
    private Integer Amount ;
    private Date TransactionDate;
    private String status;
    private Date createdAt;
}
