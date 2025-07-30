package com.jeon.auth_api.app.domain.user.web.dto;

import com.jeon.auth_api.app.domain.user.entity.User;
import com.jeon.auth_api.app.domain.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserCreateReqDto(
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3~20자 사이여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9]+(\\s[a-zA-Z0-9]+)*$", message = "사용자명은 영문, 숫자만 사용 가능하며, 공백 뒤에는 반드시 문자가 와야 합니다")
    String username,
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다")
    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]+$", 
             message = "비밀번호는 영문, 숫자, 특수문자(@$!%*?&)만 사용 가능합니다")
    String password,
    
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "닉네임은 영문과 숫자만 사용 가능합니다")
    String nickname
) {

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.username())
                .password(encodedPassword)
                .nickname(this.nickname())
                .roles(List.of(UserRole.USER))
                .build();
    }
}