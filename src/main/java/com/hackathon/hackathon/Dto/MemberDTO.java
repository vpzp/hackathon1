package com.hackathon.hackathon.Dto;

import com.hackathon.hackathon.Entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자를 자동 생성
@AllArgsConstructor // 모든 필드를 초기화하는 생성자를 생성
@ToString // tostring()메서드를 생성(객체를 문자열로 표현할 때 사용)
public class MemberDTO {
    private Long id; //필드 선언
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String age;
    private String personality;
    private String region;
    private String additionalInfo;

    public static  MemberDTO toMemberDTO(MemberEntity memberEntity) { //memberEntity -> MemberDTO 변환
        MemberDTO memberDTO = new MemberDTO(); //Entity는 데이터베이스와 직접 상호작용(테이블에 매핑), DTO는 데이터를 클라이언트와 주고받을 때 사용
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setAdditionalInfo(memberEntity.getAdditionalInfo());
        memberDTO.setRegion(memberEntity.getRegion());
        memberDTO.setPersonality(memberEntity.getPersonality());
        memberDTO.setAge(memberDTO.getAge());
        return memberDTO;
    }
}
