package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Rating;
import com.bit.cinema_manager.service.RatingService;
import com.bit.cinema_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rating")
@AllArgsConstructor
public class RatingController {
    private final RatingService RATING_SERVICE;
    private final UserService USER_SERVICE;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 극장 목록 조회
    @GetMapping("/showAll")
    public Object getAllRatingss() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", RATING_SERVICE.getAllRatings());
        resultMap.put("total", RATING_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 극장 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getRatingssByPage(@PathVariable String page) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid page number");
            return resultMap;
        }

        List<Rating> list = RATING_SERVICE.selectByPage(pageNo);
        if (list.isEmpty()) {
            resultMap.put("result", "fail");
            resultMap.put("message", "No Ratingss found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("list", list);

            int maxPage = RATING_SERVICE.selectMaxPage();
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

    // 개별 극장 조회
    @GetMapping("/showOne/{id}")
    public Object getOneRating(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        Rating rating = RATING_SERVICE.getOneRating(id);

        if (rating == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Rating not found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("rating", rating);

        }
        return resultMap;
    }

    @PostMapping("/updateReview/{id}")
    public Object updateReview(@PathVariable int id, @RequestBody Rating rating) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            rating.setId(id);
            RATING_SERVICE.updateReview(rating);
            resultMap.put("result", "succes");
            resultMap.put("Rating", rating);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }
}
