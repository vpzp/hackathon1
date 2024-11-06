package com.hackathon.hackathon.Service;

import com.hackathon.hackathon.air.AirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainService {
    private final AirRepository airRepository;
    public Map<String, List<String>> getSidoStationMap() {
        return airRepository.findSidoStationMap();
    }


}
