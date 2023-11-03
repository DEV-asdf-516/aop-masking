package com.example.masking.controller;

import com.example.masking.model.UserInfoRequestDto;
import com.example.masking.model.UserListRequestDto;
import com.example.masking.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {
    private final ExampleService service;
    @GetMapping("/userinfo")
    public ResponseEntity<Object> userInfo(UserInfoRequestDto request){
        return ResponseEntity.ok(service.getUserInfo(request));
    }
    @GetMapping("/userlist")
    public ResponseEntity<Object> userList(UserListRequestDto request){
        return ResponseEntity.ok(service.getUserInfoList(request));
    }
}
