package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.HistoryDTO;
import java.util.List;

public interface HistoryMapper {
    List<HistoryDTO> getTransfer(Integer userId);
}