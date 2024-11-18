package com.OhsunDosun.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.OhsunDosun.dto.HistoryDTO;
import com.OhsunDosun.service.HistoryService;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/get")
    public List<HistoryDTO> getTransferHistory(@RequestParam Integer userId) {
        log.info("getTransferHistory start");
        log.info("called userId={}", userId);
        try {
            return historyService.getTransferHistory(userId);
        } catch (Exception e) {
            log.error("getTransferHistory catch exception", e);
            throw e;
        }
    }
}