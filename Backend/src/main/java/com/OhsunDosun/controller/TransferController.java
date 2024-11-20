package com.OhsunDosun.controller;

import com.OhsunDosun.dto.TransferRequest;
import com.OhsunDosun.service.conversation.task.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    // 송금하기
    @PostMapping("/send")
    public ResponseEntity<String> sendMoney(@RequestBody TransferRequest transferRequest) {
        transferService.transferMoney(transferRequest);
        return ResponseEntity.ok("transfer succeed");
    }
}