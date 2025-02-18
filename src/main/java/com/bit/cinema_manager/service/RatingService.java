package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Rating;
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

    // 평점 추가
    public void addRating(Rating rating) {
        sqlSession.insert(NAMESPACE + ".addRating", rating);
    }

    // 특정 영화의 평점 목록 조회
    public List<Rating> getRatingsByMovie(int movieId) {
        return sqlSession.selectList(NAMESPACE + ".getRatingsByMovie", movieId);
    }

    // 특정 영화의 평점 업데이트
    public void updateMovieScore(int movieId, int score) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieId", movieId);
        paramMap.put("score", score);
        sqlSession.update(NAMESPACE + ".updateMovieScore", paramMap);
    }
}

