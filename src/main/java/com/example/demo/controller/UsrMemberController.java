package com.example.demo.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.MemberService;
import com.example.demo.util.Ut;
import com.example.demo.vo.Member;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

@Controller
public class UsrMemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private Rq rq;

	@RequestMapping("/usr/member/join")
	public String join() {
		return "usr/member/join";
	}
	
	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String loginId) {
		
		if(Ut.empty(loginId)) {
			return ResultData.from("F-1", "아이디를 입력해주세요");
		}
		
		ResultData<Boolean> isDupLoginIdRd = memberService.isDupLoginId(loginId);
		
		if(isDupLoginIdRd.getData1()) {
			return ResultData.from("F-2", isDupLoginIdRd.getMsg(),"loginId",loginId);
		}
		
		return ResultData.from("S-1", isDupLoginIdRd.getMsg(),"loginId",loginId);
	}
	
	@RequestMapping("/usr/member/getLoginPwConfirm")
	@ResponseBody
	public ResultData<String> getLoginPwConfirm(String loginPw, String loginPwConfirm) {
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-1", "비밀번호를 입력해주세요");
		}
		
		if (Ut.empty(loginPw)) {
			return ResultData.from("F-2", "비밀번호확인을 입력해주세요");
		}
		
		if(!loginPw.equals(loginPwConfirm)) {
			return ResultData.from("F-3", "비밀번호가 일치하지 않습니다.");
		}
		
		return ResultData.from("S-1", "비밀번호가 일치합니다.");
	}
	
	@RequestMapping("/usr/member/getEmailDup")
	@ResponseBody
	public ResultData<String> getEmailDup(String email) {
		
		if (Ut.empty(email)) {
			return ResultData.from("F-1", "이메일을 입력해주세요");
		}
		
	    String emailRegex = "\\S+@\\S+\\.\\S+"; // 이메일 유효성 검사용 정규표현식

	    Pattern pattern = Pattern.compile(emailRegex);
	    Matcher matcher = pattern.matcher(email);

	    if (!matcher.matches()) {
	    	return ResultData.from("F-2", "올바른 이메일 형식이 아닙니다.");
	    }
		
		ResultData<Boolean> isDupEmailRd = memberService.isDupEmail(email);
		
		if(isDupEmailRd.getData1()) {
			return ResultData.from("F-2", isDupEmailRd.getMsg(),"email",email);
		}
		
		return ResultData.from("S-1", "사용가능한 이메일 입니다.","email",email);
	}
	
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email, @RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(loginId)) {
			return rq.jsHistoryBack("F-1", "아이디를 입력해주세요");
		}
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}
		if (Ut.empty(name)) {
			return rq.jsHistoryBack("F-3", "이름을 입력해주세요");
		}
		if (Ut.empty(nickname)) {
			return rq.jsHistoryBack("F-4", "닉네임을 입력해주세요");
		}
		if (Ut.empty(cellphoneNum)) {
			return rq.jsHistoryBack("F-5", "전화번호를 입력해주세요");
		}
		if (Ut.empty(email)) {
			return rq.jsHistoryBack("F-6", "이메일을 입력해주세요");
		}

		ResultData<Integer> joinRd = memberService.join(loginId, Ut.sha256(loginPw), name, nickname, cellphoneNum, email);

		if (joinRd.isFail()) {
			return rq.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}

		ResultData<Member> getMemberByIdRd = memberService.getMemberById(joinRd.getData1());

		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getEncodedUri(afterLoginUri);

		return Ut.jsReplace("S-1", Ut.f("회원가입이 완료되었습니다"), afterJoinUri);
	}

	@RequestMapping("/usr/member/login")
	public String login(String loginId, String loginPw, String replaceUri) {
		
		return "usr/member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, @RequestParam(defaultValue = "/") String afterLoginUri) {

		if (Ut.empty(loginId)) {
			return Ut.jsHistoryBack("F-1", "아이디를 입력해주세요");
		}
		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("F-2", "비밀번호를 입력해주세요");
		}
		
		ResultData<Member> loginRd = memberService.login(loginId, Ut.sha256(loginPw));

		if (loginRd.getData1() == null) {
			return Ut.jsHistoryBack("F-3", Ut.f("아이디(%s)가 없습니다.", loginId));
		}
		
		if (!loginRd.getData1().getLoginPw().equals(Ut.sha256(loginPw))) {
			return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 일치하지 않습니다."));
		}
		
		rq.login(loginRd.getData1());
		
		return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다.", loginRd.getData1().getNickname()), afterLoginUri);
	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(String afterLogoutUri) {
		
		rq.logout();
		
		return Ut.jsReplace("S-1", Ut.f("로그아웃 되었습니다."), afterLogoutUri);
	}
}