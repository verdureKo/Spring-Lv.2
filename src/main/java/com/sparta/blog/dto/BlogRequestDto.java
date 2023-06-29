package com.sparta.blog.dto;

import lombok.Getter;

@Getter
public class BlogRequestDto {  // Blog Entity를 생성할 때 필요한 정보들을 담음
    private String title;
    private String contents;
}