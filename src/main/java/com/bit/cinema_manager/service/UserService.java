package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.User;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final String NAMESPACE = "mappers.UserMapper";
    private final SqlSession sqlSession;

    // 회원가입
    public void register(User user) {
//        user.setPassword(ENCODER.encoder(user.getPassword()));
        sqlSession.selectOne(NAMESPACE + ".register", user);
    }

    // 로그인
    public User logIn(User user) {
        return sqlSession.selectOne(NAMESPACE + ".logIn", user);
    }

    /*public boolean validateUserNo(User user) {
        return sqlSession.selectOne(NAMESPACE + ".validateUserNo", user) == null;
    }*/

    public User loadByUsername(String username) {
        return sqlSession.selectOne(NAMESPACE + ".loadByUsername", username);
    }

    public boolean validateUsername(User user) {
        return sqlSession.selectOne(NAMESPACE + ".validateUsername", user) == null;
    }

    public boolean validateNickname(User user) {
        return sqlSession.selectOne(NAMESPACE + ".validateNickname", user) == null;
    }

    // 회원 정보 조회
    public User getUserById(int id) {
        return sqlSession.selectOne(NAMESPACE + ".getUserId", id);
    }

    // 회원 등급 변경 (관리자)
    public void updateRole(int userId, int role) {
        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("role", role);
        sqlSession.update(NAMESPACE + ".updateRole");
    }

}
