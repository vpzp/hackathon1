package com.hackathon.hackathon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AirApiTest {
    AirApi api = new AirApi();
    @Test
    public void apiTest() throws Exception {
        api.getAirData("2024-11-02");
    }
}