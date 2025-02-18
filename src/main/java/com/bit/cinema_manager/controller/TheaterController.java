package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.Theater;
import com.bit.cinema_manager.service.TheaterService;
import com.bit.cinema_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theater")
@AllArgsConstructor
public class TheaterController {
    private final TheaterService THEATER_SERVICE;
    private final UserService USER_SERVICE;
    private final String LIST_FORMATTER = "yy-MM-dd HH:mm:ss";

    // 전체 극장 목록 조회
    @GetMapping("/showAll")
    public Object getAllTheaters() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", THEATER_SERVICE.getAllTheaters());
        resultMap.put("total", THEATER_SERVICE.countAll());
        return resultMap;
    }

    // 페이지별 극장 목록 조회
    @GetMapping("/showAll/{page}")
    public Object getTheatersByPage(@PathVariable String page) {
        Map<String, Object> resultMap = new HashMap<>();
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid page number");
            return resultMap;
        }

        List<Theater> list = THEATER_SERVICE.selectByPage(pageNo);
        if (list.isEmpty()) {
            resultMap.put("result", "fail");
            resultMap.put("message", "No Theaters found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("list", list);

            int maxPage = THEATER_SERVICE.selectMaxPage();
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
    public Object getOneTheater(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        Theater theater = THEATER_SERVICE.getOneTheater(id);

        if (theater == null) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Theater not found");
        } else {
            resultMap.put("result", "success");
            resultMap.put("theater", theater);

        }
        return resultMap;
    }

    // 극장 등록
    @PostMapping("/add")
    public Object addTheater(@RequestBody Theater theater) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            THEATER_SERVICE.addTheater(theater);
            resultMap.put("result", "success");
            resultMap.put("theater", theater);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 극장 수정
    @PostMapping("/update/{id}")
    public Object updateTheater(@PathVariable int id, @RequestBody Theater theater) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            theater.setId(id);
            THEATER_SERVICE.updateTheater(theater);
            resultMap.put("result", "success");
            resultMap.put("Theater", theater);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    // 극장 삭제
    @GetMapping("/delete/{id}")
    public Object deleteTheater(@PathVariable int id) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            THEATER_SERVICE.deleteTheater(id);
            resultMap.put("result", "success");
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }
}
