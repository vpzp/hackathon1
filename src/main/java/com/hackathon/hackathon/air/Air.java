package com.hackathon.hackathon.air;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
public class Air {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private int pm10Value;

    private String pm25Value;

    private String o3Value;

    private String informGrade;

//    구 이름
    private String stationName;

//    자치도 이름
    private String sidoName;

    //강수확률
    private int rainValue;

//    온도
    private String temperature;
}
