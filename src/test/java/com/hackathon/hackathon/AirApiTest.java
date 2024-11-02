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
        String[] sidoName = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남",
                "전북", "전남", "경북", "경남", "제주", "세종"};
        String[] date = {"2024-10-30", "2024-10-31", "2024-11-01", "2024-11-02", "2024-11-03", "2024-11-04", "2024-11-05"};
        String[] nx ={"60", "98","89","55","58", "67", "102", "60", "73", "69", "68", "63","51", "87", "91", "52", "66"};
        String[] ny ={"127", "76","90", "124","74", "100", "84", "120", "134", "107","100",  "89","67", "106", "77", "38", "103"};
        for (int i = 0; i < date.length; i++) {
            airService.getAirData(date[i]);
        }
        airService.getWeatherData(date[4], nx, ny);

    }
}