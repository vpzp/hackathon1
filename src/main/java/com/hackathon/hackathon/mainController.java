package com.hackathon.hackathon;

import com.hackathon.hackathon.air.Air;
import com.hackathon.hackathon.air.AirService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class mainController {
    private final AirService airService;

    @GetMapping("/")
    public String main(Model model){
        List<Air> airList = airService.getAllAirList();
        model.addAllAttributes(airList);

        return "mainpage";
    }

}
