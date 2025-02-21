package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Screening;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ScreeningService {
    private final String NAMESPACE = "mappers.ScreeningMapper";
    private final SqlSession sqlSession;
    private final int SIZE = 5;

    // 페이지에 따른 상영정보 목록 불러오기
    public List<Screening> selectByPage(int page) {
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (page - 1) * SIZE);
        paramMap.put("limitSize", SIZE);
        return sqlSession.selectList(NAMESPACE + ".selectByPage", paramMap);
    }

    // 상영정보 전체 목록 조회
    public List<Screening> getAllScreenings() {
        return sqlSession.selectList(NAMESPACE + ".getAllScreenings");
    }

    // 상영정보 전체 개수 조회
    public int countAll() {
        return sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
    }

    // 최대 페이지 조회
    public int selectMaxPage() {
        int temp = sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
        return temp % SIZE == 0 ? temp / SIZE : (temp / SIZE) + 1;
    }

    // 상영정보 개별 조회
    public Screening getOneScreening(String id) {
        return sqlSession.selectOne(NAMESPACE + ".getOneScreening", id);
    }

    // 상영정보 등록
    public void addScreening(Screening screening) {
        sqlSession.insert(NAMESPACE + ".addScreening", screening);
    }

    // 상영정보 수정
    public void updateScreening(Screening screening) {
        sqlSession.update(NAMESPACE + ".updateScreening", screening);
    }

    // 상영정보 삭제
    public void deleteScreening(int id) {
        sqlSession.delete(NAMESPACE + ".deleteScreening", id);
    }
}