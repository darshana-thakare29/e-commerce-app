package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.util.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        User existingUser = userService.login(user);

        // generate token using email
        String token = jwtUtil.generateToken(existingUser.getEmail());

        // greturn token in response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", UUID.nameUUIDFromBytes(existingUser.getEmail()
                .trim().toLowerCase().getBytes(StandardCharsets.UTF_8)).toString());

        return response;
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return userService.sendResetLink(email);
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        String email = request.get("email");

        return userService.resetPassword(email, newPassword);
    }


}
