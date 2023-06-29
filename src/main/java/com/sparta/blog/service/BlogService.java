package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service // @Component 기능을 포함하고 서비스 역할까지 표현하는 어노테이션
@RequiredArgsConstructor  // 생성자 자동생성 어노테이션
public class BlogService {

    private final BlogRepository blogRepository;  // 블로그 DB와 연결
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional  // 영속성 환경: 변경감지
    public BlogResponseDto createLog(BlogRequestDto requestDto, HttpServletRequest request) {

        User user = checkToken(request);  // 토큰 체크 추가

        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Blog blog = new Blog(requestDto, user);
        blogRepository.save(blog);
        return new BlogResponseDto(blog);
    }


    // 전체 Blog 조회
    @Transactional
    public List<BlogResponseDto> getLogs() {
        List<Blog> blogs = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> blogResponseDto = new ArrayList<>();

        for (Blog blog : blogs) {
            blogResponseDto.add(new BlogResponseDto(blog));
        }

        return blogResponseDto;
    }

    // 선택 Blog 조회
    @Transactional(readOnly = true)  // 트랜잭션 관리자는 읽기 전용 트랜잭션을 요청할 때 예외를 throw하지 않고 조용히 힌트를 무시합니다.
    public BlogResponseDto getLog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 일치하지 않습니다."));
        return new BlogResponseDto(blog);
    }

    // 선택 Blog 수정
    @Transactional
    public BlogResponseDto updateLog(Long id, BlogRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);

        if(user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        if (!blog.getUser().equals(user)) {
            throw new IllegalArgumentException("글 작성자가 아닙니다.");
        }

        blog.update(requestDto);
        return new BlogResponseDto(blog);
    }

    // 선택 Blog 삭제
    @Transactional
    public void deleteLog(Long id, HttpServletRequest request) {

        // 토큰 체크 추가
        User user = checkToken(request);

        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        if (blog.getUser().equals(user)) {
            blogRepository.delete(blog);
        }
    }

    public User checkToken(HttpServletRequest request){

        String token = jwtUtil.getJwtFromHeader(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }


}