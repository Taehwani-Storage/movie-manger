package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Review;
import com.bit.cinema_manager.service.ReviewService;
import com.bit.cinema_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService REVIEW_SERVICE;
    private final UserService USER_SERVICE;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 평점 목록 조회
    @GetMapping("/showAll")
    public Object getAllReviewss() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", REVIEW_SERVICE.getAllReviews());
        resultMap.put("total", REVIEW_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 평점 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getReviewssByPage(@PathVariable String page) {
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
    public Object getOnereview(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        Review review = REVIEW_SERVICE.getOneReview(id);

        if (review == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "review not found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("review", review);

        }
        return resultMap;
    }

    // 영화 평론 수정
    @PostMapping("/addReview")
    public Object addReview(@RequestBody Review review) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            REVIEW_SERVICE.addReview(review);
            resultMap.put("result", "succes");
            resultMap.put("reviewa", review);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }
}
