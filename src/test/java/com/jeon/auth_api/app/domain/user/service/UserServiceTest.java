package com.jeon.auth_api.app.domain.user.service;

import com.jeon.auth_api.app.domain.user.entity.User;
import com.jeon.auth_api.app.domain.user.entity.UserRole;
import com.jeon.auth_api.app.domain.user.repository.UserRepository;
import com.jeon.auth_api.app.domain.user.web.dto.UserCreateReqDto;
import com.jeon.auth_api.app.global.exception.CustomException;
import com.jeon.auth_api.app.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @Test
        @DisplayName("회원가입 성공 테스트")
        void signup_success_test() {
            // given
            UserCreateReqDto reqDto = new UserCreateReqDto("cheeseburger", "1234", "CheeseBurger");
            // stub : userRepository
            when(userRepository.existsByUsername(any())).thenReturn(false);
            when(userRepository.save(any())).thenReturn(
                    User.builder()
                            .id(1L)
                            .username("cheeseburger")
                            .nickname("CheeseBurger")
                            .password("1234")
                            .roles(List.of(UserRole.USER))
                            .build()
            );

            // when
            User savedUser = userService.signup(reqDto);

            // then
            assertEquals(1L, savedUser.getId());
            assertEquals("cheeseburger", savedUser.getUsername());
            assertEquals("CheeseBurger", savedUser.getNickname());
        }

        @Test
        @DisplayName("회원가입 실패 테스트 : 이미 회원이 존재하는 경우")
        void signup_duplicate_username_test() {
            // given
            UserCreateReqDto reqDto = new UserCreateReqDto("cheeseburger", "1234", "CheeseBurger");
            when(userRepository.existsByUsername(any())).thenReturn(true);

            // when & then
            CustomException customException = assertThrows(CustomException.class, () -> userService.signup(reqDto));
            assertTrue(customException.getMessage().contains(ErrorCode.ALREADY_EXIST_USER.getMessage()));
        }
    }
}