package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Movie;
import com.bit.cinema_manager.model.User;
import com.bit.cinema_manager.service.MovieService;
import com.bit.cinema_manager.service.UserService;
import com.bit.cinema_manager.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movie")
@AllArgsConstructor
@CrossOrigin("http://localhost:3001")
public class MovieController {
    private final MovieService MOVIE_SERVICE;
    private final UserService USER_SERVICE;
    private final JwtUtil JWT_UTIL;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 영화 목록 조회
    @GetMapping("/showAll")
    public Object getAllMovies() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", MOVIE_SERVICE.getAllMovies());
        resultMap.put("total", MOVIE_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 영화 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getMoviesByPage(@PathVariable String page) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid page number");
            return resultMap;
        }

        List<Movie> list = MOVIE_SERVICE.selectByPage(pageNo);
        if (list.isEmpty()) {
            resultMap.put("result", "fail");
            resultMap.put("message", "No movies found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("list", list);

            int maxPage = MOVIE_SERVICE.selectMaxPage();
            int startPage = pageNo - 2;
            int endPage = pageNo + 2;

            if (maxPage <= 5) {
                startPage = 1;
                endPage = maxPage;
            } else if (pageNo <= 3) {
                startPage = 1;
                endPage = 5;
            } else if (pageNo >= maxPage - 2) {
                startPage = maxPage - 4;
                endPage = maxPage;
            }

            resultMap.put("maxPage", maxPage);
            resultMap.put("startPage", startPage);
            resultMap.put("endPage", endPage);
            resultMap.put("currentPage", pageNo);
        }
        return resultMap;
    }

    // 개별 영화 조회
    @GetMapping("/showOne/{movieNo}")
    public Object getOneMovie(@PathVariable String movieNo, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Movie movie = MOVIE_SERVICE.getOneMovie(movieNo);

        if (!movieNo.matches("^\\d+$") || movie == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Movie not found");
        } else {
            resultMap.put("result", "success");
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);
            String username = JWT_UTIL.validateToken(token);
            User user = USER_SERVICE.loadByUsername(username);

            movie.setOwned(movie.getMovieNo() == user.getUserNo());
            resultMap.put("movie", movie);
        }
        return resultMap;
    }

    // 영화 등록
    @PostMapping("/addMoive")
    public Object addMovie(@RequestBody Movie movie, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = authHeader.substring(7);
        String username = JWT_UTIL.validateToken(token);
        User user = USER_SERVICE.loadByUsername(username);
        movie.setId(user.getId());
        try {
            MOVIE_SERVICE.addMovie(movie);
            resultMap.put("result", "success");
            resultMap.put("movie", movie);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 영화 수정
    @PostMapping("/update/{id}")
    public Object updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            movie.setId(id);
            MOVIE_SERVICE.updateMovie(movie);
            resultMap.put("result", "success");
            resultMap.put("movie", movie);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 영화 삭제
    @GetMapping("/delete/{id}")
    public Object deleteMovie(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MOVIE_SERVICE.deleteMovie(id);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
