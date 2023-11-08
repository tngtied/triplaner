package com.tngtied.triplaner.service;

import com.tngtied.triplaner.JwtAuthenticationFilter;
import com.tngtied.triplaner.JwtTokenProvider;
import com.tngtied.triplaner.UserRole;
import com.tngtied.triplaner.dto.TokenInfo;
import com.tngtied.triplaner.entity.Member;
import com.tngtied.triplaner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@SpringBootApplication
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    private final AuthenticationManagerBuilder authenticationManagerBuilder;

//    @Bean
//    public FilterRegistrationBean jwtFilter(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(jwtAuthenticationFilter);
//        registrationBean.addUrlPatterns("${base.url}"+"/user/login");
//        return registrationBean;
//    }

    public Member create(String userName, String email, String password) {
        if (userRepository.findByUsername(userName).isPresent()){
            System.out.println(">>"+userRepository.findByUsername(userName).toString());
            throw  new DataIntegrityViolationException("USERNAME");
        }
        if (userRepository.findByEmail(email).isPresent()){
            throw  new DataIntegrityViolationException("EMAIL");
        }

        Member siteUser = new Member(userName, passwordEncoder.encode(password), "USER", email);
        this.userRepository.save(siteUser);
        System.out.printf(">>user creation success with username [%s], email [%s], password [%s]\n", siteUser.getUsername(), siteUser.getEmail(), password);
        return siteUser;
    }

    @Transactional
    public TokenInfo login(String userName, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        System.out.printf(">> building token with username [%s] and password [%s]\n", userName, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);


        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //authenticationManagerBuilder.getObject() 부분이 null인 문제 지속 발생중
        System.out.println(">> Password check clear");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException(">> Username not found.");
        }
        Member siteUserFound = user.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(siteUserFound.getUsername())){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        System.out.println(">> loadUserByUsername() called, found successfully");

        return new User(siteUserFound.getUsername(), siteUserFound.getPassword(), authorities);

    }

    private UserDetails createUserDetails(Member member){
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }

}
