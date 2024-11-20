package com.OhsunDosun.service;

import com.OhsunDosun.dto.HistoryDTO;

import java.util.List;

public interface HistoryService {
    List<HistoryDTO> getTransferHistory(Integer userId);
}
