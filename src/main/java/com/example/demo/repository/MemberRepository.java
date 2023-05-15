package com.example.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Member;

@Mapper
public interface MemberRepository {

	@Insert("""
			INSERT INTO `member`
			set regDate = NOW(),
			updateDate = NOW(),
			loginId = #{loginId},
			loginPw = #{loginPw},
			`name` = #{name},
			nickname = #{nickname},
			cellphoneNum = #{cellphoneNum},
			email = #{email}
			""")
	public void doJoinMember(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email);

	@Select("""
			SELECT *
			FROM `member`
			WHERE id = #{id}
			""")
	public Member getMemberById(int id);

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE loginId = #{loginId}
			""")
	public boolean isDupLoginId(String loginId);
	
	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE email = #{email}
			""")
	public boolean isDupEmail(String email);
	
	@Select("""
			SELECT *
			FROM `member`
			WHERE loginId = #{loginId}
			""")
	public Member getMemberByLoginId(String loginId);
	
	@Select("""
			SELECT LAST_INSERT_ID()
			""")
	public int getLastInsertId();

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public boolean isDupNameAndEmail(String name, String email);
	
	@Select("""
			SELECT *
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public Member getMemberByNameAndEmail(String name, String email);
	
	@Select("""
			SELECT loginPw
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public String getLoginPwByNameAndEmail(String name, String email);

	

	
}