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

    @PostMapping("/logs")
    public BlogResponseDto createLog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.createLog(requestDto, request);
    }

    @GetMapping("/logs")
    public List<BlogResponseDto> getLogs() {
        return blogService.getLogs();
    }

    @GetMapping("/logs/{id}")
    public BlogResponseDto getLog(@PathVariable Long id) {
        return blogService.getLog(id);
    }

    @PutMapping("/logs/{id}")
    public BlogResponseDto updateLog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.updateLog(id, requestDto, request);
    }


    @DeleteMapping("/logs/{id}")
    public ApiResult deleteLog(@PathVariable Long id, HttpServletRequest request) {
        blogService.deleteLog(id, request);
        return new ApiResult("게시글 삭제 성공", HttpStatus.OK.value());
    }
}