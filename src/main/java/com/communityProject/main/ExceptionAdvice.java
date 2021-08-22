package com.communityProject.main;

import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public String handleRuntimeException(@CurrentUser Account account, HttpServletRequest request, RuntimeException e) {
        if(account != null) {
            log.info("'{}' requested '{}'", account.getNickname(), request.getRequestURL());
        } else {
            log.info("'{}' requested '{}'", "Anonymous User", request.getRequestURL());
        }

        log.error("bad request", e);
        return "error";
    }
}
