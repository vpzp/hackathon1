package com.hackathon.hackathon.air;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AirService {
    private final AirRepository airRepository;

    public void setAirdata(LocalDateTime localDateTime, int pm10Value, String informGrade){
        Air air = new Air();
        air.setDate(localDateTime)
                .setPm10Value(pm10Value)
                .setInformGrade(informGrade);

        airRepository.save(air);

    }
}
