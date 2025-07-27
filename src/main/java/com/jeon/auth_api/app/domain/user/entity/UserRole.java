package com.jeon.auth_api.app.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("일반사용자"),
    ADMIN("관리자");

    private final String value;
}
