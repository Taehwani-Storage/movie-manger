package com.bit.cinema_manager.controller;

import com.bit.cinema_manager.model.User;
import com.bit.cinema_manager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService USER_SERVICE;

    @PostMapping("/register")
    public Object register(@RequestBody User user) {
        Map<String, Object> resultMap = new HashMap<>();

        if (!USER_SERVICE.validateUsername(user)) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Duplicated username");
        } else if (!USER_SERVICE.validateNickname(user)) {
            resultMap.put("result", "fail");
            resultMap.put("message", "Duplicated nickname");
        } else {
            USER_SERVICE.register(user);
            resultMap.put("result", "success");
        }

        return resultMap;
    }

    @PostMapping("/logIn")
    public Object logIn(@RequestBody User user) {
        Map<String, Object> resultMap = new HashMap<>();
        User origin = USER_SERVICE.loadByUsername(user.getUsername());
        if (origin != null)
            resultMap.put("result", "success");
        else {
            resultMap.put("result", "fail");
            resultMap.put("message", "Check your ID");
        }
        // 토큰 활용한 로그인 기능 추가
        /*UserDTO origin = USER_SERVICE.loadByUsername(userDTO.getUsername());
        if(origin != null && PASSWORD_ENCODER.matches(userDTO.getPassword(), origin.getPassword())) {
            String token = JWT_UTIL.createToken(userDTO.getUsername());
            resultMap.put("result", "success");
            resultMap.put("token", token);
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "Check your Login Info");

        }*/

        return resultMap;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return USER_SERVICE.getUserById(id);
    }

    @PostMapping("/role/update")
    public void updateUserRole(@RequestParam int userId, @RequestParam int role) {
        USER_SERVICE.updateRole(userId, role);
    }

}
