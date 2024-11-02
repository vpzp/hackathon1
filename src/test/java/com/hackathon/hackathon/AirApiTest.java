package com.hackathon.hackathon;

import com.hackathon.hackathon.air.AirService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AirApiTest {
    @Autowired
    private  AirService airService;

    @Test
    public void apiTest() throws Exception {
        String[] date = {"2024-10-30", "2024-10-31", "2024-11-01", "2024-11-02", "2024-11-03", "2024-11-04", "2024-11-05"};
        for (int i = 0; i < date.length; i++) {
            airService.getAirData(date[i]);
        }
    }
}