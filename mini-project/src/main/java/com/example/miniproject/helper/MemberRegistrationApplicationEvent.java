package com.example.miniproject.helper;

import com.example.miniproject.model.Member;
import lombok.Getter;

@Getter
public class MemberRegistrationApplicationEvent {
    private Member member;
    public MemberRegistrationApplicationEvent(Member member) {
        this.member = member;
    }
}