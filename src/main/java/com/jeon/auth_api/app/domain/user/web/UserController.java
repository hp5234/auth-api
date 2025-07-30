package com.jeon.auth_api.app.domain.user.web;

import com.jeon.auth_api.app.domain.user.entity.User;
import com.jeon.auth_api.app.domain.user.service.UserService;
import com.jeon.auth_api.app.domain.user.web.dto.UserCreateReqDto;
import com.jeon.auth_api.app.domain.user.web.dto.UserCreateRespDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserCreateReqDto requestDto) {
        User savedUser = userService.signup(requestDto);
        return new ResponseEntity<>(UserCreateRespDto.from(savedUser), HttpStatus.CREATED);
    }
}
