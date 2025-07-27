package com.jeon.auth_api.app.global.dto;

public record CommonErrorRespDto (
    Body error
){
    public static CommonErrorRespDto of(String code, String message) {
        return new CommonErrorRespDto(new Body(code, message));
    }

    public record Body (
            String code,
            String message
    ){
    }
}
