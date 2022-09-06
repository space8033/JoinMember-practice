package com.example.miniproject.service;

import com.example.miniproject.exception.BusinessLogicalException;
import com.example.miniproject.exception.ExceptionCode;
import com.example.miniproject.model.Member;
import com.example.miniproject.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@Service
public class MemberService {
    private static MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public static Member createMember(Member member){
        verifyExistsUsername(member.getUsername());
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getUsername());

        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.setPassword(password));
        Optional.ofNullable(member.getEmail())
                .ifPresent(email -> findMember.setEmail(email));

        return memberRepository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(String username) {
        return findVerifiedMember(username);
    }

    public void deleteMember(String username) {
        Member findMember = findVerifiedMember(username);

        memberRepository.delete(findMember);
    }

    public static void verifyExistsUsername(String username){
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isPresent())
            throw new BusinessLogicalException(ExceptionCode.MEMBER_EXISTS);
    }
    @Transactional(readOnly = true)
    public Member findVerifiedMember(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicalException(ExceptionCode.MEMBER_NOT_FOUND));

        return findMember;
    }
}
