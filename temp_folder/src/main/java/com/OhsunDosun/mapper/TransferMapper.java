package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.TransferRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferMapper {

    // 송금 내역 추가
    void insertTransferHistory(TransferRequest transferRequest);

    // 송금하는 사람 잔액 업데이트
    void updateAccountBalances(TransferRequest transferRequest);

    // 송금받는 사람 잔액 업데이트
    void updateReceiverBalance(TransferRequest transferRequest);
}