package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Theater;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class TheaterService {
    private final String NAMESPACE = "mappers.TheaterMapper";
    private final SqlSession sqlSession;
    private final int SIZE = 5;

    // 페이지에 따른 극장 목록 불러오기
    public List<Theater> selectByPage(int page) {
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (page - 1) * SIZE);
        paramMap.put("limitSize", SIZE);
        return sqlSession.selectList(NAMESPACE + ".selectByPage", paramMap);
    }

    // 극장 전체 목록 조회
    public List<Theater> getAllTheaters() {
        return sqlSession.selectList(NAMESPACE + ".getAllTheaters");
    }

    // 극장 전체 개수 조회
    public int countAll() {
        return sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
    }

    // 최대 페이지 조회
    public int selectMaxPage() {
        int temp = sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
        return temp % SIZE == 0 ? temp / SIZE : (temp / SIZE) + 1;
    }

    // 극장 개별 조회
    public Theater getOneTheater(int id) {
        return sqlSession.selectOne(NAMESPACE + ".getOneTheater", id);
    }

    // 극장 등록
    public void addTheater(Theater theater) {
        sqlSession.insert(NAMESPACE + ".addTheater", theater);
    }

    // 극장 수정
    public void updateTheater(Theater theater) {
        sqlSession.update(NAMESPACE + ".updateTheater", theater);
    }

    // 극장 삭제
    public void deleteTheater(int id) {
        sqlSession.delete(NAMESPACE + ".deleteTheater", id);
    }
}