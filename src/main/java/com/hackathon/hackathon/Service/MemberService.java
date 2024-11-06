package com.hackathon.hackathon.Service;

import com.hackathon.hackathon.Dto.MemberDTO;
import com.hackathon.hackathon.Entity.MemberEntity;
import com.hackathon.hackathon.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean save(MemberDTO memberDTO) {
        // 이메일 중복 확인
        Optional<MemberEntity> existingMember = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (existingMember.isPresent()) {
            // 이미 존재하는 이메일이면 회원가입 실패
            return false;
        }

        // 1. DTO -> Entity 변환
        MemberEntity memberEntity = toMemberEntity(memberDTO);
        // 2. 저장
        memberRepository.save(memberEntity);
        return true;
    }
    public  MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        memberEntity.setAdditionalInfo(memberDTO.getAdditionalInfo());
        memberEntity.setRegion(memberDTO.getRegion());
        memberEntity.setPersonality(memberDTO.getPersonality());
        memberEntity.setAge(memberDTO.getAge());
        return memberEntity;
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                return MemberDTO.toMemberDTO(memberEntity);
            }
        }
        return null;  // 로그인 실패 시 null 반환
    }

    // 이메일로 회원 정보를 찾는 메서드 추가
    public MemberEntity findByEmail(String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberEmail(email);
        return memberEntity.orElse(null);  // 회원이 존재하지 않으면 null 반환
    }
}
