package com.hackathon.hackathon.air;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.replaceAll;

@Service
@RequiredArgsConstructor
public class AirService {
    private final AirRepository airRepository;

    public List<Air> getAllAirList() {
        return airRepository.findAll();
    }

    public Air setAirdata(Air air, String date, int pm10Value, String informGrade, String stationName,
                           String sidoName) {
        air.setDate(date)
                .setPm10Value(pm10Value)
                .setInformGrade(informGrade)
                .setStationName(stationName)
                .setSidoName(sidoName);

        airRepository.save(air);
        return air;
    }

    public void setWatherData(String sidoName, String date, String rainValue, String temparature){
        List<Air> bySidoName = airRepository.findBySidoNameAndDate(sidoName, date);
        rainValue = rainValue.replaceAll("[^0-9]", "");
        temparature = temparature.replaceAll("[^0-9]", "");

        if (rainValue == "") {
            rainValue = "0";
        }
        if (temparature == ""){
            temparature = "0";
        }


        for (Air air : bySidoName) {
            air.setRainValue(Integer.parseInt(rainValue))
                    .setTemperature(temparature);

            airRepository.save(air);
        }


    }

    public boolean isPresent(String date, String stationName) {
        if (airRepository.findByDateAndAndStationName(date, stationName).isPresent()) {
            return true;
        }
        return false;
    }

    public void getAirData(String date) throws Exception {
        String[] sidoName = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남",
                "전북", "전남", "경북", "경남", "제주", "세종"};

        for (int k = 0; k < sidoName.length; k++) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
//            serviceKey는 커밋할때 뺏음
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "Service key"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
            urlBuilder.append("&" + URLEncoder.encode("InformCode", "UTF-8") + "=" + URLEncoder.encode("PM25", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(sidoName[k], "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("searchDate", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); // 특정 날짜 추가


            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            rd.close();
            conn.disconnect();

            // JSON 응답을 파싱하여 필요한 데이터만 추출
            JSONObject jsonResponse = new JSONObject(sb.toString());
            JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONArray("items");

            System.out.println("Items array length: " + items.length()); // 배열 길이 출력

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String pm10Value = item.optString("pm10Value", "0");
                String stationName = item.optString("stationName", "N/A");
                String pm25Value = item.optString("pm25Value", "0");    // 초미세먼지(PM2.5)
                String o3Value = item.optString("o3Value", "0");        // 오존(O3)
                String sidoNameResp = item.optString("sidoName", "N/A");
                String informGrade = item.optString("khaiGrade", "N/A"); // 통합대기환경지수

                pm10Value = pm10Value.replaceAll("[^0-9]", "");
                if (pm10Value == ""){
                    pm10Value = "21";
                }
                Optional<Air> optionalAir = airRepository.findByDateAndAndStationName(date, stationName);
                if (optionalAir.isPresent()){
                    Air air = optionalAir.get();
                    setpm25ValueAndo3Value(air, pm25Value, o3Value);
                    setpm10Value(air, pm10Value);
                }
                if (isPresent(date, stationName)) {
                    continue;
                }

//                pm10Value = pm10Value.equals("-") ? "0" : pm10Value;
//
//                System.out.println("pm25Value = "+ pm25Value + "o3Value = "+ o3Value);
//                setAirdata(air, date, Integer.parseInt(pm10Value), informGrade, stationName, sidoNameResp);
            }
        }
    }

    public void setpm10Value(Air air, String pm10Value){
        air.setPm10Value(Integer.parseInt(pm10Value));

        airRepository.save(air);
    }
    public void setpm25ValueAndo3Value(Air air, String pm25Value, String o3Value){
        air.setPm25Value(pm25Value).setO3Value(o3Value);

        airRepository.save(air);
    }

    public void getWeatherData(String date, String[] nx, String[] ny) throws Exception{
        String[] sidoName = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남",
                "전북", "전남", "경북", "경남", "제주", "세종"};
        String formattedDate = date.replace("-", "");

        for (int j = 0; j < sidoName.length; j++) {
            // URL 구성
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
            //            serviceKey는 커밋할때 뺏음
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + "service key");
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + "JSON");
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + formattedDate);
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + "0500"); // 정시 기준 시간
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + nx[j]);
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + ny[j]);

            // HTTP 요청 설정
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            // 응답 코드 확인
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONObject jsonResponse = new JSONObject(sb.toString());
            String resultCode = jsonResponse.getJSONObject("response").getJSONObject("header").getString("resultCode");

            if ("03".equals(resultCode)) {
            } else {
                // 정상 데이터일 때 파싱 진행 (기온, 강수량, 강수확률 출력)
                JSONObject body = jsonResponse.getJSONObject("response").getJSONObject("body");
                JSONArray items = body.getJSONObject("items").getJSONArray("item");

                String temperature = "";
                String rainValue ="";
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String category = item.getString("category");
                    String fcstValue = item.getString("fcstValue");
                    String fcstDate = item.getString("fcstDate");
                    String fcstTime = item.getString("fcstTime");

                    if ("TMP".equals(category)) { // 기온
                        temperature = fcstValue;
                        System.out.println("기온: " + fcstValue + "°C, 날짜: " + fcstDate + ", 시간: " + fcstTime);
                    }  else if ("POP".equals(category)) { // 강수확률
                        rainValue = fcstValue;
                        System.out.println("강수확률: " + fcstValue + "%, 날짜: " + fcstDate + ", 시간: " + fcstTime);
                    }

                }
                setWatherData(sidoName[j], date, rainValue, temperature);
            }


        }

    }
}

