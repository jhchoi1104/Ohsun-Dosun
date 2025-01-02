package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.TransferRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferMapper {
    void insertTransferHistory(TransferRequest transferRequest);
    void updateAccountBalances(TransferRequest transferRequest);
    void updateReceiverBalance(TransferRequest transferRequest);
}