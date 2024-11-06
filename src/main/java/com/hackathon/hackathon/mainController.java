package com.hackathon.hackathon;

import com.hackathon.hackathon.Entity.MemberEntity;
import com.hackathon.hackathon.Service.MainService;
import com.hackathon.hackathon.Service.MemberService;
import com.hackathon.hackathon.air.Air;
import com.hackathon.hackathon.air.AirService;
import com.hackathon.hackathon.chatGpt.ChatgptService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@Slf4j
public class mainController {
    private final AirService airService;
    private final MainService mainService;
    private final ChatgptService chatgptService;
    private final MemberService memberService;

    @GetMapping("/")
    public String main(Model model){
        List<Air> airList = airService.getAllAirList();
        Map<String, List<String>> sidoStationMap = mainService.getSidoStationMap();
        model.addAttribute("airList", airList);
        model.addAttribute("sidoStationMap", sidoStationMap);

        System.out.println(sidoStationMap.get("서울"));
        System.out.println("airList = " + airList.size());

        return "mainpage";
    }

    @GetMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public String test(Model model ,Principal principal) {
        // ChatGPT에 보낼 메시지

        log.info("principal 아이디는 {}", principal.getName());
        MemberEntity member = memberService.findByEmail(principal.getName());
        String age = member.getAge();
        String personality = member.getPersonality();
        String region = member.getRegion();
        String additionalInfo = member.getAdditionalInfo();
        String message = "나이는 : "+age + " 성격은 : " + personality + " 사는곳은 : "+ region+ " 추가로 할말은 : "+additionalInfo
                +"\n 이 정보들을 토대로 여행지 혹은 놀거리를 5개 추천해주라 \n형식은"  +
                "이미지 : "+
                "이름 : " +
                "내용 : " +
                "위치 : " + "이런형식으로 key value값으로 대답해줘!\n" +
                "이미지는 google에 검색해서 나온 첫번째 사진을 주소값 포함해서 .jpg형식으로 보내주라 \n" +
                "내용은 35자 이상으로 그 놀거리를 묘사해줘 "+
                "잘 하면 돈을 줄거야";

        System.out.println("message = " + message);
        // ChatGPT 서비스에서 응답 받기
        String chatGptResponse = chatgptService.getChatResponse(message);

        System.out.println("ChatGPT 응답: " + chatGptResponse);

        // 모델에 응답 추가
        model.addAttribute("response", chatGptResponse);

        Map<String, Map<String, String>> locationsMap = parseChatGptResponse(chatGptResponse);
        locationsMap.forEach((name, details) -> {
            System.out.println("장소 이름: " + name);
            details.forEach((key, value) -> System.out.println("  " + key + ": " + value));
            System.out.println();
        });

        System.out.println("locationsMap = " + locationsMap.size());
        model.addAttribute("locationsMap", locationsMap);


        // 응답을 test.html로 전달
        return "test";
    }

    public Map<String, Map<String, String>> parseChatGptResponse(String response) {
        Map<String, Map<String, String>> locationsMap = new HashMap<>();

        Pattern pattern = Pattern.compile(
                "\"이름\": \"(.*?)\",\\s+\"내용\": \"(.*?)\",\\s+\"위치\": \"(.*?)\"",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(response);

        // 일치 여부 확인용 플래그
        boolean foundMatch = false;

        // 장소마다 이름, 내용, 위치를 추출하여 Map에 저장
        while (matcher.find()) {
            foundMatch = true;
            String name = matcher.group(1).trim();
            String description = matcher.group(2).trim();
            String location = matcher.group(3).trim();

            // 각 장소의 세부 정보를 저장할 Map
            Map<String, String> detailsMap = new HashMap<>();
            detailsMap.put("내용", description);
            detailsMap.put("위치", location);

            // 장소 이름을 키로 하고, 세부 정보를 값으로 저장
            locationsMap.put(name, detailsMap);
        }

        // 일치하는 항목이 없는 경우 경고 출력
        if (!foundMatch) {
            System.out.println("경고: 'parseChatGptResponse'에서 일치하는 항목을 찾지 못했습니다. 정규 표현식이 데이터 형식과 일치하는지 확인하세요.");
        }

        return locationsMap;
    }

}
