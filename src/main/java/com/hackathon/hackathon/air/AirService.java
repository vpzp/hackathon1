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

@Service
@RequiredArgsConstructor
public class AirService {
    private final AirRepository airRepository;

    public List<Air> getAllAirList(){
        return airRepository.findAll();
    }

    public void setAirdata(String date, int pm10Value, String informGrade, String stationName,
                           String sidoName){
        Air air = new Air();
        air.setDate(date)
                .setPm10Value(pm10Value)
                .setInformGrade(informGrade)
                .setStationName(stationName)
                .setSidoName(sidoName);

        airRepository.save(air);

    }

    public boolean isPresent(String date, String stationName){
        if (airRepository.findByDateAndAndStationName(date, stationName).isPresent()) {
            return true;
        }
        return false;
    }

    public void getAirData(String date) throws Exception {
        String[] sidoName = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남",
                "전북", "전남", "경북", "경남", "제주", "세종"};

        for(int k = 0 ; k < sidoName.length ; k++){
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=fzmCfsH9p8b0uEteCyJHVkpuxV7D3NYn2bRNXjoeJNcEIqlzxveUggHdcaiZtPlWajR30Fo9jswNxq%2Fgdwcs2A%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
            urlBuilder.append("&" + URLEncoder.encode("InformCode", "UTF-8") + "=" + URLEncoder.encode("PM10", "UTF-8")); /*통보코드검색(PM10, PM25, O3)*/
            urlBuilder.append("&" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(sidoName[k], "UTF-8"));


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
            rd.close();
            conn.disconnect();

            // JSON 응답을 파싱하여 필요한 데이터만 추출
            JSONObject jsonResponse = new JSONObject(sb.toString());
            JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String pm10Value = item.optString("pm10Value", "0");
                String stationName = item.optString("stationName", "N/A");
                String sidoNameResp = item.optString("sidoName", "N/A");
                String informGrade = item.optString("khaiGrade", "N/A"); // 통합대기환경지수

                if (isPresent(date, stationName)){
                    continue;
                }

                pm10Value = pm10Value.equals("-") ? "0" : pm10Value;
                setAirdata(date, Integer.parseInt(pm10Value), informGrade, stationName, sidoNameResp);
            }
        }
    }
}
