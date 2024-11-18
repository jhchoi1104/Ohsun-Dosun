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
public class HistoryDTO {
    private Integer transactionId;
    private Integer senderId;
    private Integer receiverId;
    private Integer amount ;
    private Date transactionDate;
    private String status;
    private Date createdAt;
}
