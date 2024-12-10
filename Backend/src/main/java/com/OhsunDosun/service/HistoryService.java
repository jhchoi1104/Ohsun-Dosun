package com.OhsunDosun.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.OhsunDosun.mapper.HistoryMapper;
import com.OhsunDosun.dto.HistoryDTO;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryMapper mapper;

    public List<HistoryDTO> getTransferHistory(Integer userId) {
        log.info("service getHistory Called, userId={}", userId);
        return mapper.getTransfer(userId);
    }
}