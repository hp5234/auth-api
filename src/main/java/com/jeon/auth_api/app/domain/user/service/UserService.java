package com.jeon.auth_api.app.domain.user.service;

import com.jeon.auth_api.app.domain.user.entity.User;
import com.jeon.auth_api.app.domain.user.entity.UserRole;
import com.jeon.auth_api.app.domain.user.repository.UserRepository;
import com.jeon.auth_api.app.domain.user.web.dto.UserCreateReqDto;
import com.jeon.auth_api.app.global.exception.CustomException;
import com.jeon.auth_api.app.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param request 회원가입 요청 DTO
     * @return 생성된 사용자 정보
     */
    @Transactional
    public User signup(UserCreateReqDto request) {
        // 1. 사용자명 중복 검사
        validateUsername(request.username());

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 3. 사용자 엔티티 생성 (기본 역할: USER)
        User user = User.builder()
                .username(request.username())
                .password(encodedPassword)
                .nickname(request.nickname())
                .roles(List.of(UserRole.USER))
                .build();

        // 4. 사용자 저장
        return userRepository.save(user);
    }

    // 중복검사
    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER, HttpStatus.BAD_REQUEST);
        }
    }
}
