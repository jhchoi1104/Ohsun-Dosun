package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.HistoryDTO;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper
public interface HistoryMapper {
    List<HistoryDTO> getTransfer(Integer userId);
}