package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Rating;
import com.bit.cinema_manager.model.Screening;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RatingService {
    private final String NAMESPACE = "mappers.RatingMapper";
    private final SqlSession sqlSession;
    private final int SIZE = 3;

    // 페이지에 따른 평점 목록 불러오기
    public List<Rating> selectByPage(int page) {
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (page - 1) * SIZE);
        paramMap.put("limitSize", SIZE);
        return sqlSession.selectList(NAMESPACE + ".selectByPage", paramMap);
    }

    // 평점 전체 목록 조회
    public List<Rating> getAllRatings() {
        return sqlSession.selectList(NAMESPACE + ".getAllRatings");
    }

    // 평점 전체 개수 조회
    public int countAll() {
        return sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
    }
    
    // 평점 개별 조회
    public Rating getOneRating(int id) {
        return sqlSession.selectOne(NAMESPACE + ".getOneRating", id);
    }

    // 최대 페이지 조회
    public int selectMaxPage() {
        int temp = sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
        return temp % SIZE == 0 ? temp / SIZE : (temp / SIZE) + 1;
    }


    // 영화 평점 업데이트 (전체 사용자 평균값 반영)
    public void updateMovieScore(int movieId, int score) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieId", movieId);
        paramMap.put("score", score);

        sqlSession.update(NAMESPACE + ".updateMovieScore", paramMap);
    }

    // 사용자의 평론 추가 또는 수정
    public void updateReview(Rating rating) {
        /*Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieId", movieId);
        paramMap.put("userId", userId);
        paramMap.put("review", review);*/
        sqlSession.update(NAMESPACE + ".updateReview", rating);
    }
}

