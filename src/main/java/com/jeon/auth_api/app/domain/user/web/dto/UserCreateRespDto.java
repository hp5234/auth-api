package com.jeon.auth_api.app.domain.user.web.dto;

import com.jeon.auth_api.app.domain.user.entity.User;

import java.util.List;

public record UserCreateRespDto(
    String username,
    String nickname,
    List<RoleDto> roles
) {

    public static UserCreateRespDto from(User user) {
        return new UserCreateRespDto(
                user.getUsername(),
                user.getNickname(),
                user.getRoles().stream()
                        .map(e -> new RoleDto(e.name()))
                        .toList()
        );
    }

    public record RoleDto(String role) { }
}
