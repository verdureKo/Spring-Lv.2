package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.ApiResult;
import com.sparta.blog.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;

    // Blog 작성 API
    @PostMapping("/logs")
    public BlogResponseDto createLog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {  // 객체 형식으로 넘어오기 때문에 RequestBody를 사용
        return blogService.createLog(requestDto, request);
    }

    // 전체 Blog 조회 API
    @GetMapping("/logs")
    public List<BlogResponseDto> getLogs() {
        return blogService.getLogs();
    }

    // 튜터님 피드백: path의 {id}로 글 1개인 것을 알 수 있다, URI 컨벤션을 참고하면 단수<복수
    // 선택 Blog 조회 API
    @GetMapping("/logs/{id}")
    public BlogResponseDto getLog(@PathVariable Long id) {
        return blogService.getLog(id);
    }

    // Blog 수정 API
    @PutMapping("/logs/{id}")
    public BlogResponseDto updateLog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.updateLog(id, requestDto, request);
    }


    // Blog 삭제 API
    @DeleteMapping("/logs/{id}")
    public ApiResult deleteLog(@PathVariable Long id, HttpServletRequest request) {
        blogService.deleteLog(id, request);
        return new ApiResult("게시글 삭제 성공", HttpStatus.OK.value()); // 게시글 삭제 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
    }
}