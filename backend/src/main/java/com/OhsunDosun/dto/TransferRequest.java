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

    private int senderId;
    private int receiverId;
    private String receiverAccountNumber;
    private String receiverAccountBank;
    private int amount;
    private String transactionDate;

}