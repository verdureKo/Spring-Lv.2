package com.sparta.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult {

    private String message;
    private int statusCode;

    @Builder
    public ApiResult(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
