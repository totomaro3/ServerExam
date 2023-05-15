package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.repository.MemberRepository;
import com.example.demo.util.Ut;
import com.example.demo.vo.Member;
import com.example.demo.vo.ResultData;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}
		
		if(memberRepository.isDupEmail(email)) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이메일(%s)입니다", email));
		}
		
		memberRepository.doJoinMember(loginId, loginPw, name, nickname, cellphoneNum, email);
		
		int id =  memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", nickname+"회원이 가입되었습니다.","id", id);
		
	}

	public ResultData<Member> getMemberById(int id) {
		
		Member member = memberRepository.getMemberById(id);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","member", member);
	}

	public ResultData<Member> login(String loginId, String loginPw) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		if(member == null) {
			return ResultData.from("F-1", Ut.f("아이디가 없습니다."));
		}
		
		return ResultData.from("S-1", Ut.f("%s님 환영합니다.", member.getNickname()),"member", member);
	}

	public ResultData<Boolean> isDupLoginId(String loginId) {
		
		if(memberRepository.isDupLoginId(loginId)) {
			return ResultData.from("F-2", "중복된 아이디 입니다.","isDupLoginId", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 아이디 입니다.","isDupLoginId", false);
		}
	}
	
	public ResultData<Boolean> isDupEmail(String email) {
		if(memberRepository.isDupEmail(email)) {
			return ResultData.from("F-2", "중복된 이메일 입니다.","isDupEmail", true);
		}
		else {
			return ResultData.from("S-1", "사용 가능한 이메일 입니다.","isDupEmail", false);
		}
	}
	
	public ResultData<Member> getMemberByLoginId(String loginId) {
		
		Member member = memberRepository.getMemberByLoginId(loginId);
		
		return ResultData.from("S-1", "멤버를 찾았습니다.","Member", member);
	}

	
}
