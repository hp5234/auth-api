package com.jeon.auth_api.app.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeon.auth_api.app.global.dto.CommonErrorRespDto;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CustomAuthRespUtil {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthRespUtil.class);

    public static void success(HttpServletResponse response, Object dto, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            String responseBody = om.writeValueAsString(dto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("파싱 에러 : ", e);
        }
    }

    public static void fail(HttpServletResponse response, String code, String message, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            CommonErrorRespDto responseDto = CommonErrorRespDto.of(code, message);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("파싱 에러 : ", e);
        }
    }
}
