package com.communityProject.account;

import com.communityProject.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @BeforeEach
    public void beforeEach() { }

    @DisplayName("checkEmailTokenWithWrongInput")
    @Test
    public void checkEmailTokenWithWrongInput() throws Exception{
        mockMvc.perform(get("/check-email-token")
                .param("token", "asdfasdf")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));
    }

    @DisplayName("checkEmailTokenWithProperInput")
    @Test
    public void checkEmailTokenWithProperInput() throws Exception{
        Account account = Account.builder()
                .email("test@email.com")
                .password("12345678")
                .nickname("chanho").build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                .param("token", newAccount.getEmailCheckToken())
                .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("chanho"));
    }

    @DisplayName("testSignUpFormView")
    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());
    }

    @DisplayName("signUpTestWithWrongInput")
    @Test
    public void signUpSubmitWithWrongInput() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "kimchanho")
                .param("email", "asdd...")
                .param("password", "12345")
                .with(csrf())) // 이 부분을 넣어줘야함
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("signUpTestWithProperInput")
    @Test
    public void signUpSubmitWithProperInput() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "kimchanho")
                .param("email", "email@naver.com")
                .param("password", "1234567890")
                .with(csrf())) // 이 부분을 넣어줘야함
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("kimchanho"));

        Account account = accountRepository.findByEmail("email@naver.com");

        assertNotNull(account);
        assertNotEquals(account.getPassword(), "1234567890");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}