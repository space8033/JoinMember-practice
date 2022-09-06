package com.example.miniproject.controller;

import com.example.miniproject.dto.MemberDto;
import com.example.miniproject.dto.SingleResponseDto;
import com.example.miniproject.mapper.MemberMapper;
import com.example.miniproject.model.Member;

import com.example.miniproject.service.MemberService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping
@Validated
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberController(MemberService memberService, MemberMapper memberMapper, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/")
    public @ResponseBody String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @PostMapping("/join")
    public ResponseEntity postMember(@RequestBody MemberDto.Post requestBody){
        Member member = memberMapper.memberPostToMember(requestBody);
        member.setRole("ROLE_USER");
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);

        Member createdMember = MemberService.createMember(member);
        MemberDto.Response response = memberMapper.memberToMemberResponse(createdMember);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{user-name}")
    public ResponseEntity patchMember(@PathVariable("user-name") String username, @RequestBody MemberDto.Patch requestBody){
        requestBody.setUsername(username);
        requestBody.setPassword(bCryptPasswordEncoder.encode(requestBody.getPassword()));

        Member member = memberService.updateMember(memberMapper.memberPatchToMember(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToMemberResponse(member)),HttpStatus.OK
        );
    }

    @GetMapping("/{user-name}")
    public ResponseEntity getMember(
            @PathVariable("user-name") String username) {
        Member member = memberService.findMember(username);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToMemberResponse(member))
                , HttpStatus.OK);
    }

    @DeleteMapping("/{user-name}")
    public ResponseEntity deleteMember(
            @PathVariable("user-name") String username) {
        memberService.deleteMember(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "info";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "data";
    }
}
