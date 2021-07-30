package com.CommunityBlogProject.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component // Spring 은 빈과 빈들사이에만 주입 가능
@RequiredArgsConstructor // <- private final 객체 자동 생성
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signupForm = (SignUpForm) target;
        if(accountRepository.existsByEmail(signupForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signupForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if(accountRepository.existsByNickname(signupForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signupForm.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
