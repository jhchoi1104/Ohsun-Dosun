package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private int senderId; // 송금하는 사람의 user_id
    private int receiverId; // 송금받는 사람의 user_id
    private String receiverAccountNumber; // 송금받는 사람 계좌 번호
    private String receiverAccountBank; // 송금받는 사람 계좌 은행
    private int amount; // 송금 금액
    private String transactionDate; // 송금 날짜

}