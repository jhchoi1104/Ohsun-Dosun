package com.OhsunDosun.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.OhsunDosun.mapper.HistoryMapper;

@Slf4j
@Service
@RequiredArgsConstructor

public class HistoryServiceImpl implements HistoryService {
    private final HistoryMapper mapper;
}
