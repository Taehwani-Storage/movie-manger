package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Review;
import com.bit.cinema_manager.model.User;
import com.bit.cinema_manager.service.ReviewService;
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
@RequestMapping("/api/review")
@AllArgsConstructor
@CrossOrigin("http://localhost:3001")
public class ReviewController {
    private final ReviewService REVIEW_SERVICE;
    private final UserService USER_SERVICE;
    private final JwtUtil JWT_UTIL;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 평점 목록 조회
    @GetMapping("/showAll")
    public Object getAllReviews() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", REVIEW_SERVICE.getAllReviews());
        resultMap.put("total", REVIEW_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 평점 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getReviewsByPage(@PathVariable String page) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid page number");
            return resultMap;
        }

        List<Review> list = REVIEW_SERVICE.selectByPage(pageNo);
        if (list.isEmpty()) {
            resultMap.put("result", "fail");
            resultMap.put("message", "No Reviews found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("list", list);

            int maxPage = REVIEW_SERVICE.selectMaxPage();
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

    // 개별 평점 조회
    @GetMapping("/showOne/{id}")
    public Object getOneReview(@PathVariable String id, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        Review review = REVIEW_SERVICE.getOneReview(id);

        if (!id.matches("^\\d+$") || review == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Review not found");
        } else {
            resultMap.put("result", "success");
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);
            String username = JWT_UTIL.validateToken(token);
            User user = USER_SERVICE.loadByUsername(username);

            review.setOwned(review.getId() == user.getId());
            resultMap.put("review", review);
        }
        return resultMap;
    }

    // 영화 평론 수정
    @PostMapping("/addReview")
    public Object addReview(@RequestBody Review review, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);
        String username = JWT_UTIL.validateToken(token);
        User user = USER_SERVICE.loadByUsername(username);

        review.setId(user.getId());
        try {
            REVIEW_SERVICE.addReview(review);
            resultMap.put("result", "success");
            resultMap.put("review", review);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }
}
