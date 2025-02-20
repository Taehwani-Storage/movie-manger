package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Movie;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MovieService {
    private final String NAMESPACE = "mappers.MovieMapper";
    private final SqlSession sqlSession;
    private final int SIZE = 5;

    // 페이지에 따른 영화 목록 불러오기
    public List<Movie> selectByPage(int page) {
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (page - 1) * SIZE);
        paramMap.put("limitSize", SIZE);
        return sqlSession.selectList(NAMESPACE + ".selectByPage", paramMap);
    }

    // 영화 전체 목록 조회
    public List<Movie> getAllMovies() {
        return sqlSession.selectList(NAMESPACE + ".getAllMovies");
    }

    // 영화 전체 개수 조회
    public int countAll() {
        return sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
    }

    // 최대 페이지 조회
    public int selectMaxPage() {
        int temp = sqlSession.selectOne(NAMESPACE + ".selectMaxPage");
        return temp % SIZE == 0 ? temp / SIZE : (temp / SIZE) + 1;
    }

    // 영화 개별 조회
    public Movie getOneMovie(int id) {
        return sqlSession.selectOne(NAMESPACE + ".getOneMovie", id);
    }

    // 영화 등록
    public void addMovie(Movie movie) {
        sqlSession.insert(NAMESPACE + ".addMovie", movie);
    }

    // 영화 수정
    public void updateMovie(Movie movie) {
        sqlSession.update(NAMESPACE + ".updateMovie", movie);
    }

    // 영화 삭제
    public void deleteMovie(int id) {
        sqlSession.delete(NAMESPACE + ".deleteMovie", id);
    }
}
