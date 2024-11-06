package com.hackathon.hackathon.Controller;

import com.hackathon.hackathon.Dto.MemberDTO;
import com.hackathon.hackathon.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/user/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/user/save")
    public String save(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        boolean saveResult = memberService.save(memberDTO);
        if (saveResult) {
            return "redirect:/user/login?SaveSuccess=true";  // 회원가입 성공 시 로그인 페이지로 이동
        } else {
            model.addAttribute("errorMessage", "이미 존재하는 아이디입니다.");  // 회원가입 실패 시 메시지 전달
            return "save";  // 회원가입 페이지로 다시 이동
        }
    }

    @GetMapping("/user/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/home?logoutSuccess=true";  // 로그아웃 시 홈 페이지로
    }

}