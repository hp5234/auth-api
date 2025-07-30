package com.jeon.auth_api.app.domain.user.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeon.auth_api.app.domain.user.entity.User;
import com.jeon.auth_api.app.domain.user.entity.UserRole;
import com.jeon.auth_api.app.domain.user.service.UserService;
import com.jeon.auth_api.app.domain.user.web.dto.UserCreateReqDto;
import com.jeon.auth_api.app.global.exception.CustomException;
import com.jeon.auth_api.app.global.exception.ErrorCode;
import com.jeon.auth_api.app.global.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("회원가입")
    class Signup {

        @BeforeEach
        void setup() {
            mockMvc = MockMvcBuilders.standaloneSetup(userController)
                    .setControllerAdvice(new GlobalExceptionHandler())
                    .build();        }

        @Test
        @DisplayName("회원가입 성공")
        void signup_success() throws Exception {

            // given
            UserCreateReqDto reqDto = new UserCreateReqDto("user123", "password123", "nickname");
            when(userService.signup(any(UserCreateReqDto.class))).thenReturn(
                    User.builder()
                            .id(1L)
                            .username("user123")
                            .password("encodedPassword")
                            .nickname("nickname")
                            .roles(List.of(UserRole.USER))
                            .build()
            );

            // when
            ResultActions resultActions = mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)));
            // then
            resultActions
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("user123"))
                    .andExpect(jsonPath("$.roles[0].role").value("USER"));
        }

        @Test
        @DisplayName("회원가입 실패 - 유효성 검사 실패")
        void signup_valid_fail_test() throws Exception {
            // given
            UserCreateReqDto reqDto = new UserCreateReqDto(
                    "",
                    "",
                    "");

            // when
            ResultActions resultActions = mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.error.message", containsString("username")))
                    .andExpect(jsonPath("$.error.message", containsString("password")))
                    .andExpect(jsonPath("$.error.message", containsString("nickname")));
        }

        @Test
        @DisplayName("회원가입 실패 - 중복 사용자명")
        void signup_duplicate_username_fail() throws Exception {
            // given
            UserCreateReqDto reqDto = new UserCreateReqDto("user123", "password123", "nickname");
            when(userService.signup(any(UserCreateReqDto.class)))
                    .thenThrow(new CustomException(ErrorCode.ALREADY_EXIST_USER, HttpStatus.BAD_REQUEST));

            // when
            ResultActions resultActions = mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)));

            // then
            resultActions.andExpect(status().isBadRequest());
        }
    }
}