package com.hackathon.hackathon.Entity;

import com.hackathon.hackathon.Dto.MemberDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberEmail;
    private String memberName;
    private String memberPassword;
    private String age;
    private String personality;
    private String region;
    private String additionalInfo;

    // 기본 생성자
    public MemberEntity() {}

    // getters와 setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    // DTO를 MemberEntity로 변환하는 메서드
    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setAdditionalInfo(memberDTO.getAdditionalInfo());
        memberEntity.setRegion(memberDTO.getRegion());
        memberEntity.setPersonality(memberDTO.getPersonality());
        memberEntity.setAge(memberDTO.getAge());
        return memberEntity;
    }
}
