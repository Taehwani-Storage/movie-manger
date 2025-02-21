package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Review;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final String NAMESPACE = "mappers.ReviewMapper";
    private final SqlSession sqlSession;
    private final int SIZE = 3;

    // 페이지에 따른 평점 목록 불러오기
    public List<Review> selectByPage(int page) {
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (page - 1) * SIZE);
        paramMap.put("limitSize", SIZE);
        return sqlSession.selectList(NAMESPACE + ".selectByPage", paramMap);
    }

    // 평점 전체 목록 조회
    public List<Review> getAllReviews() {
        return sqlSession.selectList(NAMESPACE + ".getAllReviews");
    }

    // 평점 전체 개수 조회
    public int countAll() {
        return sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
    }
    
    // 평점 개별 조회
    public Review getOneReview(String id) {
        return sqlSession.selectOne(NAMESPACE + ".getOneReview", id);
    }

    // 최대 페이지 조회
    public int selectMaxPage() {
        int temp = sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
        return temp % SIZE == 0 ? temp / SIZE : (temp / SIZE) + 1;
    }

    // 평론 추가
    public void addReview(Review review) {
        sqlSession.insert(NAMESPACE + ".addReview", review);
    }
}

