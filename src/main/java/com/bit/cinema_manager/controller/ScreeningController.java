package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Movie;
import com.bit.cinema_manager.model.Screening;
import com.bit.cinema_manager.model.User;
import com.bit.cinema_manager.service.RatingService;
import com.bit.cinema_manager.service.ScreeningService;
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
@RequestMapping("/api/screening")
@AllArgsConstructor
public class ScreeningController {
    private final ScreeningService SCREENING_SERVICE;
    private final UserService USER_SERVICE;
    private final JwtUtil JWT_UTIL;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 상영정보 목록 조회
    @GetMapping("/showAll")
    public Object getAllScreenings() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", SCREENING_SERVICE.getAllScreenings());
        resultMap.put("total", SCREENING_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 상영정보 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getScreeningsByPage(@PathVariable String page) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid page number");
            return resultMap;
        }

        List<Screening> list = SCREENING_SERVICE.selectByPage(pageNo);
        if (list.isEmpty()) {
            resultMap.put("result", "fail");
            resultMap.put("message", "No Screening found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("list", list);

            int maxPage = SCREENING_SERVICE.selectMaxPage();
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

    // 개별 상영정보 조회
    @GetMapping("/showOne/{id}")
    public Object getOneScreening(@PathVariable String id, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Screening screening = SCREENING_SERVICE.getOneScreening(id);

        if (!id.matches("^\\d+$") || screening == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Screening not found");
        } else {
            resultMap.put("result", "success");
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);
            String username = JWT_UTIL.validateToken(token);
            User user = USER_SERVICE.loadByUsername(username);
            screening.setOwned(screening.getId() == user.getId());

            resultMap.put("screening", screening);

        }
        return resultMap;
    }

    // 상영정보 등록
    @PostMapping("/addScreening")
    public Object addScreening(@RequestBody Screening screening, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);
        String username = JWT_UTIL.validateToken(token);
        User user = USER_SERVICE.loadByUsername(username);
        screening.setId(user.getId());
        try {
            SCREENING_SERVICE.addScreening(screening);
            resultMap.put("result", "success");
            resultMap.put("movie", screening);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 상영정보 수정
    @PostMapping("/update/{id}")
    public Object updateScreening(@PathVariable int id, @RequestBody Screening screening) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            screening.setId(id);
            SCREENING_SERVICE.updateScreening(screening);
            resultMap.put("result", "success");
            resultMap.put("movie", screening);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 상영정보 삭제
    @GetMapping("/delete/{id}")
    public Object deleteMovie(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SCREENING_SERVICE.deleteScreening(id);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
