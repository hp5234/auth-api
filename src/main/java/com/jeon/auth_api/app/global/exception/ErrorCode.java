package com.jeon.auth_api.app.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다."),
    ALREADY_EXIST_USER("이미 존재하는 사용자명입니다.")
    ;

    private final String message;
}
